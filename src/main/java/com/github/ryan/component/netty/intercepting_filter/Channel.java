package com.github.ryan.component.netty.intercepting_filter;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * A nexus to a network socket or a component which is capable of
 * I/O operations such as read, write, connect, and bind.
 * A channel has a ChannelPipeline which handles all I/O events and requests
 * associated with the channel.
 *
 * @className: Channel
 * @date July 18,2018
 */
public interface Channel {

    // ChannelId id();

    /**
     * Return the EventLoop this Channel was registered to.
     */
    // EventLoop eventLoop();

    // Channel parent();

    // ChannelConfig config();

    // boolean isOpen();

    // boolean isRegistered();

    /**
     * Return true if the Channel is active and so connected.｀
     */
    // boolean isActive();

    /**
     * Returns true if and only if the I/O thread will perform the
     * requested write operation immediately. Any write requests made when
     * this method returns false are queued until the I/O thread is
     * ready to process the queued write request.
     */
    // boolean isWritable();

    /**
     * Returns an internal-use-only object that provides unsafe operations.
     */
    // Unsafe unsafe();

    // ByteBufAllocator alloc();

    ChannelPipeline pipeline();

    interface Unsafe {

        /**
         * Register the Channel of the ChannelPromise and notify
         * the ChannelFuture once the registration was completed.
         */
        // void register(EventLoop eventLoop, ChannelPromise promise);

        // void bind(SocketAddress localAddress, ChannelPromise promise)

        // void connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise);

        // void close(ChannelPromise promise)

        // void write(Object msg, ChannelPromise promise);

        // void flush();

        // ... 参见Netty
    }

}
