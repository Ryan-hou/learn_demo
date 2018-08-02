package com.github.ryan.component.netty.decorator_pattern;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * A random and sequential accessible sequence of zero or more bytes(octets).
 * This interface provides an abstract view for one or more primitive byte
 * arrays({@code byte[]}) and {@linkplain java.nio.ByteBuffer NIO buffers}.
 *
 * ByteBuf provides two pointer variables to support sequential read and
 * write operations - readerIndex for a read operation and writerIndex
 * for a write operation respectively.
 * invariant(区间左闭右开):
 * 0 <= readerIndex <= writerIndex <= capacity
 *
 * @className: ByteBuf
 * @date August 02,2018
 */
public abstract class ByteBuf implements ReferenceCounted {

    /**
     * Returns the number of bytes(octets) this buffer can contain.
     */
    public abstract int capacity();

    /**
     * Returns the {@code readerIndex} of this buffer.
     */
    public abstract int readerIndex();

    /**
     * Sets the {@code readerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code readerIndex} is
     *                                   less than {@code 0} or greater than {@code this.writerIndex}
     */
    public abstract ByteBuf readerIndex(int readerIndx);

    /**
     * Returns the {@code writerIndex} of this buffer.
     */
    public abstract int writerIndex();

    /**
     * Sets the {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code writerIndex} is
     *                                   less than {@code this.readerIndex} or
     *                                   greater than {@code this.capacity}
     */
    public abstract ByteBuf writerIndex(int writerIndex);

    /**
     * Returns the number of readable bytes which is equal to
     * {@code (this.writerIndex - this.readerIndex)}.
     */
    public abstract int readableBytes();

    /**
     * Returns the number of writable bytes which is equal to
     * {@code (this.capacity - this.writerIndex)}.
     */
    public abstract int writableBytes();

    /**
     * Returns {@code true}
     * if and only if {@code (this.writerIndex - this.readerIndex)}
     * is greater than {@code 0}.
     */
    public abstract boolean isReadable();

    /**
     * Returns a slice of this buffer's readable bytes. Modifying the content
     * of the returned buffer or this buffer affects each other's content
     * while they maintain separate indexes and marks. This method is identical
     * to {@code buf.slice(buf.readerIndex(), buf.readableBytes())}.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * <p>
     * Also be aware that this method will NOT call {@link #retain()} and so
     * the reference count will NOT be increased.
     */
    public abstract ByteBuf slice();

    public abstract ByteBuf slice(int index, int length);

    /**
     * Determines if the content of the specified buffer is identical to the
     * content of this array. 'Identical' here means:
     * <p>
     * the size of the contents of the two buffers are same and
     * every single byte of the content of the two buffers are same.
     * <p>
     * Please note that it does not compare {@link #readerIndex()} nor
     * {@link #writerIndex()}. This method also returns {@code false} for
     * {@code null} and an object which is not an instance of {@link ByteBuf} type.
     */
    @Override
    public abstract boolean equals(Object obj);

    /**
     * Returns a hash code which wae calculated from the content of this
     * buffer. If there's a byte array which is
     * {@linkplain #equals(Object) equal to} this array, both arrays should
     * return the same value.
     */
    @Override
    public abstract int hashCode();

    @Override
    public abstract ByteBuf retain();

    @Override
    public abstract ByteBuf retain(int increment);

}
