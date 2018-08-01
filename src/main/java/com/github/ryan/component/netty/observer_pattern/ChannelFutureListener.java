package com.github.ryan.component.netty.observer_pattern;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * Listens to the result of a {@link ChannelFuture}. The result of
 * the asynchronous {@link io.netty.channel.Channel} I/O operation is
 * notified once this listener is added by calling
 * {@link ChannelFuture#addListener(GenericFutureListener)}.
 *
 * Return the control to the caller quickly.
 * {@link #operationComplete(Future)} is directly called by an I/O
 * thread. Therefore, performing a time consuming task or a blocking
 * operation in the handler method can cause an unexpected pause during
 * I/O. If you need to perform a blocking operation on I/O completion,
 * try to execute the operation in a different thread using a thread
 * pool.
 *
 * @className: ChannelFutureListener
 * @date August 01,2018
 */
public interface ChannelFutureListener extends GenericFutureListener<ChannelFuture> {

    // Just a type alias

    /**
     * A {@link ChannelFutureListener} that closes the {@link io.netty.channel.Channel}
     * which is associated with the specified {@link ChannelFuture}.
     */
    ChannelFutureListener CLOSE = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            future.channel().close();
        }
    };

    ChannelFutureListener CLOSE_ON_FAILURE = future -> {
        if (!future.isSuccess()) {
            future.channel().close();
        }
    };
}
