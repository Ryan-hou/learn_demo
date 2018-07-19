package com.github.ryan.component.netty.intercepting_filter;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * 责任链模式 -- 动态添加／删除责任处理器
 * 使用双向链表
 * ChannelPipeline是由ChannelHandlerContext构成的双向链表，每一个ChannelHandlerContext封装了对应的
 * ChannelHandler和其上下文，通过ChannelPipeline和ChannelHandlerContext，事件沿着ChannelHandlers
 * 进行传播
 *
 * A list of ChannelHandlers which handles or intercepts inbound events and
 * outbound operations of a Channel.
 * ChannelPipeline implements an advanced form of the Intercepting Filter pattern
 * to give a user full control over how an event is handled and how the ChannelHandlers
 * in a pipeline interact with each other.
 *
 * @className: ChannelPipeline
 * @date July 18,2018
 */
public interface ChannelPipeline {

    /**
     * Inserts a ChannelHandler at the first position of this pipeline.
     *
     * @param name the name of the handler to insert first
     * @param handler the handler to insert first
     *
     * @throws IllegalArgumentException
     *      if there's an entry with the same name already in the pipeline
     * @throws NullPointerException
     *      if the specified handler is null
     */
    ChannelPipeline addFirst(String name, ChannelHandler handler);

    /**
     * Appends a ChannelHandler at the last position of this pipeline.
     *
     * @param name  the name of the handler to append
     * @param handler   the handler to append
     *
     * @throws IllegalArgumentException
     *      if there's an entry with the same name already in the pipeline
     * @throws NullPointerException
     *      if the specified handler is null
     */
    ChannelPipeline addLast(String name, ChannelHandler handler);

    /**
     * Removes the last ChannelHandler in this pipeline.
     *
     * @return the removed handler
     *
     * @throws java.util.NoSuchElementException
     *      if this pipeline is empty
     */
    ChannelHandler removeLast();

    /**
     * Returns the Channel this pipeline is attached to.
     *
     * @return the channel. null if this pipeline is not attached yet.
     */
    Channel channel();


    /**
     * A Channel received a message.
     *
     * This will result in having the ChannelInboundHandler#channelRead(ChannelHandlerContext, Object)
     * method called of the next ChannelInboundHandler in the ChannelPipeline.
     */
    ChannelPipeline fireChannelRead(Object msg);
}
