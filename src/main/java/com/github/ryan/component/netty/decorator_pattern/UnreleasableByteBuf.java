package com.github.ryan.component.netty.decorator_pattern;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * 装饰者：
 * A {@link ByteBuf} implementation that wraps another buffer to prevent a user
 * from increasing or decreasing the wrapped buffer's reference count.
 *
 * @className: UnreleasableByteBuf
 * @date August 02,2018
 */
final class UnreleasableByteBuf extends WrappedByteBuf {

    public UnreleasableByteBuf(ByteBuf buf) {
        super(buf);
    }

    @Override
    public ByteBuf retain() {
        return this;
    }

    @Override
    public ByteBuf retain(int increment) {
        return this;
    }

    @Override
    public boolean release() {
        return false;
    }

    @Override
    public boolean release(int decrement) {
        return false;
    }

    @Override
    public ByteBuf slice() {
        return new UnreleasableByteBuf(buf.slice());
    }

    @Override
    public ByteBuf slice(int index, int length) {
        return new UnreleasableByteBuf(buf.slice(index, length));
    }
}
