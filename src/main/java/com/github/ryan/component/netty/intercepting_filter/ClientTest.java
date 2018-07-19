package com.github.ryan.component.netty.intercepting_filter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: ClientTest
 * @date July 19,2018
 */
@Slf4j
public class ClientTest {

    public static void main(String[] args) {
        // 创建channel时，会同时创建该channel对应的ChannelPipeline
        Channel channel = new DefaultChannel();
        // 向ChannelPipeline添加自定义的业务处理器Handler
        channel.pipeline().addLast(null, new Test1Handler()).addLast(null, new Test2Handler());
        channel.pipeline().fireChannelRead("Hello world");
    }
}
