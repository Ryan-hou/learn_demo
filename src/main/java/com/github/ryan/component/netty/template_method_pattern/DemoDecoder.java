package com.github.ryan.component.netty.template_method_pattern;

import com.github.ryan.component.netty.time.pojo.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * @author ryan.houyl@gmail.com
 * @description: Dealing with a Stream-based Transport:
 * deal with the fragmentation issue, also a ChannelInboundHandler
 * @className: DemoDecoder
 * @date July 26,2018
 */
public class DemoDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }

        // demo
        out.add(new UnixTime(in.readUnsignedInt()));
    }
}
