package com.github.ryan.component.netty.intercepting_filter;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * 责任链模式 －－ 责任处理器上下文
 * Enables a ChannelHandler to interact with its ChannelPipeline and other handlers.
 * A ChannelHandler is provided with a ChannelHandlerContext object. A ChannelHandler is
 * supposed to interact with the ChannelPipeline it belongs to via a context object.
 *
 * Please note:
 * A ChannelHandler instance can be added to more than one ChannelPipeline. It means a single
 * ChannelHandler instance can have more than one ChannelHandlerContext and therefore the
 * single instance can be invoked with different ChannelHandlerContexts if it is added to
 * one or more ChannelPipelines more than once.
 *
 * @className: ChannelHandlerContext
 * @date July 18,2018
 */
public interface ChannelHandlerContext {

    /**
     * The ChannelHandler that is bound this ChannelHandlerContext.
     */
    ChannelHandler handler();

    /**
     * Return the Channel which is bound to the ChannelHandlerContext.
     */
    Channel channel();

    /**
     *  Return the assigned ChannelPipeline.
     */
    ChannelPipeline pipeline();

    /**
     *  The unique name of the ChannelHandlerContext.The name is used when the
     *  ChannelHandler was added to the ChannelPipeline. This name can also be used
     *  to access the registered ChannelHandler from the ChannelPipeline.
     */
    String name();


    /**
     * A Channel received a message.
     *
     * This will result in having the ChannelInboundHandler#channelRead(ChannelHandlerContext, Object)
     * method called of the next ChannelInboundHandler contained in the ChannelPipeline of the
     * Channel.
     * @param msg
     * @return
     */
    ChannelHandlerContext fireChannelRead(Object msg);

    /**
     * Request to close the Channel and notify ChannelFuture once the operation completes,
     * either because the operation was successful or because of and error.
     *
     * After it is closed it is not possible to reuse it again.
     * This will result in having the ChannelOutboundHandler#close(ChannelHandlerContext, ChannelPromise)
     * method called of the next ChannelOutboundHandler contained in the ChannelPipeline of the Channel.
     */
    // ChannelFuture close();

}
