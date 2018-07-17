package com.github.ryan.component.netty.time;

import com.github.ryan.component.netty.time.pojo.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * It's much simpler than writing a decoder because there's no need to
 * deal with packet fragmentation and assembly when encoding a message
 * @className: TimeEncoder
 * @date July 06,2018
 */
public class TimeEncoder extends ChannelOutboundHandlerAdapter {

    // ChannelPromise: Special ChannelFuture which is writable
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        UnixTime m = (UnixTime) msg;
        ByteBuf encoded = ctx.alloc().buffer(4);
        encoded.writeInt((int) m.value());
        ctx.write(encoded, promise);
        // we did not call ctx.flush(), there is a separate handler method void flush(ChannelHandlerContext ctx)
        // which is purposed to override the flush() operation
    }
}
