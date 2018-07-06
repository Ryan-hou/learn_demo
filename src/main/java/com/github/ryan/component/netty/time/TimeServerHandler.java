package com.github.ryan.component.netty.time;

import com.github.ryan.component.netty.time.pojo.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: TimeServerHandler
 * @date July 06,2018
 */
@Slf4j
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    // channelActive() method will be invoked when a connection is established
    // and ready to generate traffic.
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
//        final ByteBuf time = ctx.alloc().buffer(4);
//        // ByteBuf has two pointers for read and write, no need to flip!
//        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
//
//        final ChannelFuture f = ctx.writeAndFlush(time);
//        f.addListener(new ChannelFutureListener() {
//            public void operationComplete(ChannelFuture future) throws Exception {
//                assert f == future;
//                ctx.close();
//            }
//        });
//        // Simplify above code using a pre-defined listener
//        // f.addListener(ChannelFutureListener.CLOSE);

        // use pojo with encoder
        ChannelFuture f = ctx.writeAndFlush(new UnixTime());
        f.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("TimeServerHandler exception!", cause);
        ctx.close();
    }
}
