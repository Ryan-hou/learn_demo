package com.github.ryan.component.netty.observer_pattern;


import io.netty.channel.Channel;
import io.netty.util.concurrent.EventExecutor;


/**
 * @author ryan.houyl@gmail.com
 * @description:
 * A skeletal {@link ChannelFuture} implementation which represents
 * a {@ChannelFuture} which has been completed already.
 *
 * @className: CompleteChannelFuture
 * @date August 01,2018
 */
abstract class CompleteChannelFuture extends CompleteFuture<Void> implements ChannelFuture {

    private final Channel channel;

    /**
     * Creates a new instance.
     *
     * @param channel the {@link Channel} associate with this future
     */
    protected CompleteChannelFuture(Channel channel, EventExecutor executor) {
        super(executor);
        if (channel == null) {
            throw new NullPointerException("channel");
        }
        this.channel = channel;
    }

    @Override
    protected EventExecutor executor() {
        EventExecutor e = super.executor();
        if (e == null) {
            return channel().eventLoop();
        } else {
            return e;
        }
    }

    @Override
    public ChannelFuture removeListener(GenericFutureListener<? extends Future<? super Void>> listener) {
        super.removeListener(listener);
        return this;
    }

    @Override
    public ChannelFuture addListener(GenericFutureListener<? extends Future<? super Void>> listener) {
        super.addListener(listener);
        return this;
    }

    @Override
    public ChannelFuture sync() throws InterruptedException {
        return this;
    }

    @Override
    public ChannelFuture syncUninterruptibly() {
        return this;
    }

    @Override
    public ChannelFuture await() throws InterruptedException {
        return this;
    }

    @Override
    public ChannelFuture awaitUninterruptibly() {
        return this;
    }

    @Override
    public Channel channel() {
        return channel;
    }

    @Override
    public Void getNow() {
        return null;
    }
}
