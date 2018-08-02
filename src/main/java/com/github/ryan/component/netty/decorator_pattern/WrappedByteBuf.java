package com.github.ryan.component.netty.decorator_pattern;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * Wraps another {@link ByteBuf}
 *
 * 装饰者模式：
 * 1）装饰者和被装饰者继承同一个接口(这里即ByteBuf)
 * 2）装饰者给被装饰者动态修改行为
 * WrappedByteBuf 方法实现均为转发，
 * 继承该类便于动态的修改需要被修改的被装饰者的行为
 *
 * @className: WrappedByteBuf
 * @date August 02,2018
 */
class WrappedByteBuf extends ByteBuf {

    protected final ByteBuf buf;

    protected WrappedByteBuf(ByteBuf buf) {
        if (buf == null) {
            throw new NullPointerException("buf");
        }

        this.buf = buf;
    }

    @Override
    public final int capacity() {
        return buf.capacity();
    }

    @Override
    public final int readerIndex() {
        return buf.readerIndex();
    }

    @Override
    public final ByteBuf readerIndex(int readerIndx) {
        buf.readerIndex(readerIndx);
        return this;
    }

    @Override
    public final int writerIndex() {
        return buf.writerIndex();
    }

    @Override
    public final ByteBuf writerIndex(int writerIndex) {
        buf.writerIndex(writerIndex);
        return this;
    }

    @Override
    public final int readableBytes() {
        return buf.readableBytes();
    }

    @Override
    public final int writableBytes() {
        return buf.writableBytes();
    }

    @Override
    public final boolean isReadable() {
        return buf.isReadable();
    }

    @Override
    public ByteBuf slice() {
        return buf.slice();
    }

    @Override
    public ByteBuf slice(int index, int length) {
        return buf.slice(index, length);
    }

    @Override
    public boolean equals(Object obj) {
        return buf.equals(obj);
    }

    @Override
    public int hashCode() {
        return buf.hashCode();
    }

    @Override
    public ByteBuf retain() {
        buf.retain();
        return this;
    }

    @Override
    public ByteBuf retain(int increment) {
        buf.retain(increment);
        return this;
    }

    @Override
    public int refCnt() {
        return buf.refCnt();
    }

    @Override
    public boolean release() {
        return buf.release();
    }

    @Override
    public boolean release(int decrement) {
        return buf.release(decrement);
    }
}
