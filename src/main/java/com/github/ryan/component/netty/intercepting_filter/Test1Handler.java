package com.github.ryan.component.netty.intercepting_filter;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: Test1Handler
 * @date July 19,2018
 */
@Slf4j
public class Test1Handler extends DefaultChannelHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("{} read message: {}", StringUtil.simpleClassName(Test1Handler.class), msg);
        // 向后传播read事件
        ctx.fireChannelRead(msg);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.error("Handler {} added to ChannelPipeline.", StringUtil.simpleClassName(Test1Handler.class));
    }
}
