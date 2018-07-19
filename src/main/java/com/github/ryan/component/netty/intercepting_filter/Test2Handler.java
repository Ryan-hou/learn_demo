package com.github.ryan.component.netty.intercepting_filter;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: Test2Handler
 * @date July 19,2018
 */
@Slf4j
public class Test2Handler extends DefaultChannelHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("{} read message: {}", StringUtil.simpleClassName(Test2Handler.class), msg);
        // 可以增加逻辑判断，决定是否要执行业务处理器Handler的处理逻辑或者是否要继续向后传播
        log.info("{} handler processed read message", StringUtil.simpleClassName(Test2Handler.class));

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.error("Handler {} added to ChannelPipeline.", StringUtil.simpleClassName(Test2Handler.class));
    }
}
