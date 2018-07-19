package com.github.ryan.component.netty.intercepting_filter;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * Handles an I/O event or intercepts an I/O operation, and forwards it to
 * its next handler in its ChannelPipeline
 *
 * 责任链模式 －－ 责任处理器接口
 * @className: ChannelHandler
 * @date July 18,2018
 */
public interface ChannelHandler {

    /**
     * Invoked when the current Channel has read a message from the peer.
     */
    void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception;

    /**
     * Gets called after the ChannelHandler was added to the actual context
     * and it's ready to handle events.
     */
    void handlerAdded(ChannelHandlerContext ctx) throws Exception;
}
