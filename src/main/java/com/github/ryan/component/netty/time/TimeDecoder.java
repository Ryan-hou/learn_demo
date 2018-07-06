package com.github.ryan.component.netty.time;

import com.github.ryan.component.netty.time.pojo.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * Dealing with a Stream-based Transport:
 * deal with the fragmentation issue,
 * also a ChannelInboundHandler
 * @className: TimeDecoder
 * @date July 06,2018
 */
public class TimeDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }

        // out.add(in.readBytes(4));
        out.add(new UnixTime(in.readUnsignedInt()));
    }
}
