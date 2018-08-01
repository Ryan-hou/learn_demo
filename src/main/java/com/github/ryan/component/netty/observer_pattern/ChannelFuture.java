package com.github.ryan.component.netty.observer_pattern;


import io.netty.channel.Channel;

/**
 * The result of an asynchronous {@link io.netty.channel.Channel} I/O operation.
 *
 * All I/O operations in Netty are asynchronous. It means any I/O calls will
 * return immediately with no guarantee that the requested I/O operation has
 * been completed at the end of the call. Instead, you will be returned with
 * a {@link ChannelFuture} instance which gives you the information about the
 * result or status of the I/O operation.
 *
 * A {@link ChannelFuture} is either uncompleted or completed.
 * If the I/O operation is finished either successfully, with
 * failure, or by cancellation, the future is marked as
 * completed with more specific information, such as the cause of the
 * failure.
 *
 * Prefer {@link #addListener(GenericFutureListener)} to {@link #await()}
 * {@link #addListener(GenericFutureListener)} is non-blocking. It simply adds
 * the specified {@link ChannelFutureListener} to the {@link ChannelFuture},
 * and I/O thread will notify the listeners when the I/O operation associated
 * with the future is done.
 * {@link ChannelFutureListener} yields the best performance and resource
 * utilization because it does not block at all, but it could be tricky
 * to implement a sequential logic if you are not used to event-driven programming.
 *
 * Do NOT call {@link #await()} inside ChannelHandler
 * The event handler methods in ChannelHandler are usually called by an I/O thread.
 * if {@link #await()} is called by an event handler method, which is called by
 * the I/O thread, the I/O operation it is waiting for might never complete because
 * {@link #await()} can block the I/O operation it is waiting for, which is a
 * dead lock.
 *
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: ChannelFuture
 * @date July 31,2018
 */
public interface ChannelFuture extends Future<Void> {

    /**
     * Returns a channel where the I/O operation associated with this
     * future takes place.
     */
    Channel channel();

    @Override
    ChannelFuture addListener(GenericFutureListener<? extends Future<? super Void>> listener);

    @Override
    ChannelFuture removeListener(GenericFutureListener<? extends Future<? super Void>> listener);

    @Override
    ChannelFuture sync() throws InterruptedException;

    @Override
    ChannelFuture syncUninterruptibly();

    @Override
    ChannelFuture await() throws InterruptedException;

    @Override
    ChannelFuture awaitUninterruptibly();
}
