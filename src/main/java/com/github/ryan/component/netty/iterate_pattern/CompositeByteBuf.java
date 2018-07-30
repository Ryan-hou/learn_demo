package com.github.ryan.component.netty.iterate_pattern;

import io.netty.buffer.ByteBufAllocator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static io.netty.util.internal.ObjectUtil.checkNotNull;

/**
 * A virtual buffer which shows multiple buffers as a single merged buffer.
 * It is recommended to use {@link ByteBufAllocator#compositeBuffer()} or
 * {@link io.netty.buffer.Unpooled#wrappedBuffer(io.netty.buffer.ByteBuf...)}
 * instead of calling the constructor explicitly.
 *
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: CompositeByteBuf
 * @date July 27,2018
 */
public class CompositeByteBuf extends AbstractByteBuf implements Iterable<ByteBuf> {

    private static final Iterator<ByteBuf> EMPTY_ITERATOR = Collections.<ByteBuf>emptyList().iterator();

    private final List<Component> components;
    private final int maxNumComponents;

    public CompositeByteBuf(int maxNumComponents) {

        /**
         * this.alloc = alloc;
         * this.direct = direct;
         */
        this.maxNumComponents = maxNumComponents;
        components = newList(maxNumComponents);
    }

    /**
     * Add the given {@link ByteBuf} and increase the {@code writerIndex} if {@code increaseWriterIndex} is
     * {@code true}.
     *
     * {@link io.netty.buffer.ByteBuf#release()} ownership of {@code buffer} is transferred to this {@link CompositeByteBuf}
     * @param buffer the {@link ByteBuf} to add. ByteBuf#realease() ownership is transferred to this {@link CompositeByteBuf}
     */
    public CompositeByteBuf addComponent(boolean increaseWriterIndex, ByteBuf buffer) {
        checkNotNull(buffer, "buffer");
        addComponent0(increaseWriterIndex, components.size(), buffer);
        // consolidateIfNeeded();
        return this;
    }

    /**
     * Precondition is that {@code buffer != null}.
     */
    private int addComponent0(boolean increaseWriter, int cIndex, ByteBuf buffer) {
        assert buffer != null;
        boolean wasAdded = false;
        try {
            // checkComponentIndex(cIndex);

            int readableBytes = buffer.readableBytes();

            // No need to consolidate - just add a component to the list.
            Component c = new Component(buffer.slice());
            if (cIndex == components.size()) {
                wasAdded = components.add(c);
                if (cIndex == 0) {
                    c.endOffset = readableBytes;
                } else {
                    Component prev = components.get(cIndex - 1);
                    c.offset = prev.endOffset;
                    c.endOffset = c.offset + readableBytes;
                }
            } else {
                components.add(cIndex, c);
                wasAdded = true;
                if (readableBytes != 0) {
                    updateComponentOffsets(cIndex);
                }
            }

            if (increaseWriter) {
                writerIndex(writerIndex() + buffer.readableBytes());
            }
            return cIndex;
        } finally {
            if (!wasAdded) {
                // Think about it!
                // If added success, the ByteBuf#relase() ownership of buffer is transferred to
                // this CompositeByteBuf

                // buffer.release();
            }
        }
    }

    private void updateComponentOffsets(int cIndex) {
        int size = components.size();
        if (size <= cIndex) {
            return;
        }

        Component c = components.get(cIndex);
        if (cIndex == 0) {
            c.offset = 0;
            c.endOffset = c.length;
            cIndex ++;
        }

        // 后移
        for (int i = cIndex; i < size; i ++) {
            Component prev = components.get(i - 1);
            Component cur = components.get(i);
            cur.offset = prev.endOffset;
            cur.endOffset = cur.offset + cur.length;
        }
    }

    private static List<Component> newList(int maxNumComponents) {
        //return new ArrayList<>(Math.min(AbstractByteBufAllocator.DEFAULT_MAX_COMPONENTS, maxNumComponents));
        return new ArrayList<>(maxNumComponents);
    }

    @Override
    protected byte _getByte(int index) {
        Component c = findComponent(index);
        return c.buf.getByte(index - c.offset);
    }

    // 二分查找(offset有序)：netty的实现在components的size为0时，会存在NPE
    private Component findComponent(int offset) {
        // checkIndex(offset);

        for (int low = 0, high = components.size() - 1; low <= high;) {
            int mid = low + (high - low) / 2;
            Component c = components.get(mid);
            if (offset >= c.endOffset) {
                low = mid + 1;
            } else if (offset < c.offset) {
                high = mid - 1;
            } else {
                // c.offset =< offset < c.endOffset
                assert c.length != 0;
                return c;
            }
        }

        return null;

    }

    @Override
    public CompositeByteBuf writerIndex(int writerIndex) {
        return (CompositeByteBuf) super.writerIndex(writerIndex);
    }

    @Override
    // 取最后一个Component(包装了ByteBuf)的endOffset
    public int capacity() {
        final int numComponents = components.size();
        if (numComponents == 0) {
            return 0;
        }
        return components.get(numComponents - 1).endOffset;
    }

    // 实现Iterable<ByteBuf>接口，支持for-each循环，遍历构成CompositeByteBuf的每一个ByteBuf
    @Override
    public Iterator<ByteBuf> iterator() {
        // ensureAccessible();
        if (components.isEmpty()) {
            return EMPTY_ITERATOR;
        }
        return new CompositeByteBufIterator();
    }

    private static final class Component {
        final ByteBuf buf;
        final int length;
        // [offset, endOffset)
        int offset;
        int endOffset;

        public Component(ByteBuf buf) {
            this.buf = buf;
            length = buf.readableBytes();
        }

        void freeIfNecessary() {
            // buf.release();
            // We should not get a NPE here. If so, it must be a bug.
        }
    }

    private final class CompositeByteBufIterator implements Iterator<ByteBuf> {
        private final int size = components.size();
        private int index;

        @Override
        public boolean hasNext() {
            return size > index;
        }

        @Override
        public ByteBuf next() {
            if (size != components.size()) {
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            try {
                return components.get(index++).buf;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Read-Only");
        }
    }
}
