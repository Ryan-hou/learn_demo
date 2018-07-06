package com.github.ryan.component.netty.time;

import com.github.ryan.component.netty.time.pojo.UnixTime;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: TimeClientHandler
 * @date July 06,2018
 */
@Slf4j
public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf m = (ByteBuf) msg;
//        try {
//           long curTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
//            System.out.println(new Date(curTimeMillis));
//            ctx.close();
//        } finally {
//            m.release();
//        }

        UnixTime m = (UnixTime) msg;
        System.out.println(m);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("TimeClientHandler call error!", cause);
        ctx.close();
    }
}
