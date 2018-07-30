package com.github.ryan.component.netty.iterate_pattern;

import lombok.extern.slf4j.Slf4j;

/**
 * A skeletal implementation of a buffer.
 *
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: AbstractByteBuf
 * @date July 27,2018
 */
@Slf4j
public abstract class AbstractByteBuf extends ByteBuf {

    int readerIndex;
    int writerIndex;

    // 子类钩子方法
    // 每一个具体Buffer实现类(比如：CompositeByteBuf／UnpooledHeapByteBuf等)均实现该方法
    protected abstract byte _getByte(int index);

    @Override
    public int writerIndex() {
        return writerIndex;
    }

    @Override
    public ByteBuf writerIndex(int writerIndex) {
        if (writerIndex < readerIndex || writerIndex > capacity()) {
            throw new IndexOutOfBoundsException(String.format(
                "writerIndex: %d (expected: readerIndex(%d) <= writerIndex <= capacity(%d))",
                writerIndex, readerIndex, capacity()));
        }
        this.writerIndex = writerIndex;
        return this;
    }

    @Override
    public ByteBuf setIndex(int readerIndex, int writerIndex) {
        if (readerIndex < 0
                || readerIndex > writerIndex
                || writerIndex > capacity()) {
            throw new IndexOutOfBoundsException(String.format(
                    "readerIndex: %d, writerIndex: %d (expected: 0 <= readerIndex <= writerIndex <= capacity(%d))",
                    readerIndex, writerIndex, capacity()));
        }
        setIndex0(readerIndex, writerIndex);
        return this;
    }

    @Override
    public ByteBuf slice() {
        //todo
        return null;
    }

    @Override
    // 模版方法
    public int forEachByte(ByteProcessor processor) {
        // ensureAccessible();
        try {
            return forEachByteAsc0(readerIndex, writerIndex, processor);
        } catch (Exception e) {
            // Raises an exception bypassing compiler checks for checked exception
            throw (RuntimeException) e;

        }
    }

    @Override
    public byte getByte(int index) {
        // checkIndex(index);
        return _getByte(index);
    }

    private int forEachByteAsc0(int start, int end, ByteProcessor processor) throws Exception {
        for (; start < end; ++start) {
            if (!processor.process(_getByte(start))) {
                return start;
            }
        }
        return -1;
    }

    final void setIndex0(int readerIndex, int writerIndex) {
        this.readerIndex = readerIndex;
        this.writerIndex = writerIndex;
    }

    @Override
    public int readableBytes() {
        return writerIndex - readerIndex;
    }
}
