package com.github.ryan.component.netty.intercepting_filter;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: DefaultChannelHandler
 * @date July 19,2018
 */
public class DefaultChannelHandler implements ChannelHandler {


    /**
     * Calls ChannelHandlerContext#fireChannelRead(Object) to forward
     * to the next ChannelInboundHandler in the ChannelPipeline.
     *
     * Sub-classes may override this method to changer behavior.
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.fireChannelRead(msg);
    }

    /**
     * Do nothing by default, sub-classes may override this method.
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // NOOP
    }
}
