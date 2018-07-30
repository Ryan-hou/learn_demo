package com.github.ryan.component.netty.iterate_pattern;

/**
 * A random and sequential accessible sequence of zero or more bytes(octets).
 * This interface provides an abstract view for one or more primitive byte
 * arrays({@code byte[]}) and {@linkplain java.nio.ByteBuffer NIO buffers}.
 *
 * Creation of a buffer:
 * It is recommended to create a new buffer using the helper methods in
 * {@link io.netty.buffer.Unpooled} rather than calling an individual implementation's
 * constructor.
 *
 * Random Access Indexing:
 * Just like an ordinary primitive byte array, ByteBuf uses zero-based indexing.
 *
 * Sequential Access Indexing:
 * {@link ByteBuf} provides two pointer variables to support sequential
 * read and write operations - readerIndex and writerIndex
 *
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: ByteBuf
 * @date July 27,2018
 */
public abstract class ByteBuf {

    /**
     * Returns the number of bytes(octets) this buffer can contain.
     */
    public abstract int capacity();

    /**
     * Returns the number of readable bytes which is equal to
     * this.writerIndex - this.readerIndex.
     */
    public abstract int readableBytes();

    /**
     * Gets a byte at the specified absolute {@code index} in this buffer.
     * This method does not modify {@code readerIndex} or {@code writerIndex}
     * of this buffer.
     *
     * @throws IndexOutOfBoundsException
     *      if the specified {@code index} is less than {@code 0} or
     *      {@code index + 1} is greater than {@code this.capacity}
     */
    public abstract byte getByte(int index);

    /**
     * Returns the {@code writerIndex} of this buffer.
     */
    public abstract int writerIndex();

    /**
     * Sets the {@code writerIndex} of this buffer.
     *
     * @throws IndexOutOfBoundsException
     *      if the specified {@code writerIndex} is
     *          less than {@code this.readerIndex} or
     *          greater than {@code this.capacity}
     */
    public abstract ByteBuf writerIndex(int writerIndex);

    /**
     * Returns a slice of this buffer's readable byte. Modifying the content
     * of the returned buffer or this buffer affects each other's content
     * while they maintain separate indexes and marks. This method is
     * identical to {@code buf.slice(buf.readerIndex(), buf.writerIndex())}.
     * This method does not modify {@code readerIndex} or {@code writerIndex} of
     * this buffer.
     * <p>
     * Also be aware that this method will NOT call retain() and so the
     * reference count will NOT be increased.
     */
    public abstract ByteBuf slice();

    /**
     * Iterates over the readable bytes of this buffer with the specified {@code processor}
     * in ascending order.
     *
     * @return {@code -1} if the processor iterated to or beyond the end of the readable bytes.
     *      The last-visited index if the {@link ByteProcessor#process(byte)} returned {@code false}.
     */
    public abstract int forEachByte(ByteProcessor processor);

    /**
     * Sets the {@code readerIndex} and {@code writerIndex} of this buffer
     * in one shot.
     *
     * @param readerIndex
     * @param writerIndex
     * @return
     */
    public abstract ByteBuf setIndex(int readerIndex, int writerIndex);

}
