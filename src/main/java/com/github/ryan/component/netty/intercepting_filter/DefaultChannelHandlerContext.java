package com.github.ryan.component.netty.intercepting_filter;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: DefaultChannelHandlerContext
 * @date July 18,2018
 */
public class DefaultChannelHandlerContext extends AbstractChannelHandlerContext {

    private final ChannelHandler handler;

    public DefaultChannelHandlerContext(DefaultChannelPipeline pipeline, String name, ChannelHandler handler) {
        super(pipeline, name);

        if (handler == null) {
            throw new NullPointerException("handler");
        }
        this.handler = handler;
    }

    @Override
    public ChannelHandler handler() {
        return handler;
    }
}
