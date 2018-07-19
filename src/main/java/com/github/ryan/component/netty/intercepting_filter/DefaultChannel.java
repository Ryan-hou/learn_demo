package com.github.ryan.component.netty.intercepting_filter;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: DefaultChannel
 * @date July 19,2018
 */
public class DefaultChannel implements Channel {

    // private final Channel parent;
    // private final ChannelId id;
    // private final Unsafe unsafe;
    // private final CloseFuture closeFuture = new CloseFuture(this);
    private final DefaultChannelPipeline pipeline;

    // private volatile EventLoop eventLoop;
    // private volatile boolean registered;
    // private volatile SocketAddress localAddress;
    // private volatile SocketAddress remoteAddress;


    public DefaultChannel() {
        this.pipeline = new DefaultChannelPipeline(this);
    }

    @Override
    public ChannelPipeline pipeline() {
        return pipeline;
    }
}
