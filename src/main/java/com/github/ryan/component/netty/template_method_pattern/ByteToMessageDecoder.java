package com.github.ryan.component.netty.template_method_pattern;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderException;
import io.netty.util.internal.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * {@link ChannelInboundHandlerAdapter} which decodes bytes in a stream-like fashion from
 * one {@link io.netty.buffer.ByteBuf} to an other Message type.
 *
 * Frame detection:
 * Generally frame detection should be handled earlier in the pipeline by adding
 * a {@link io.netty.handler.codec.DelimiterBasedFrameDecoder},
 * {@link io.netty.handler.codec.FixedLengthFrameDecoder}
 * {@link io.netty.handler.codec.LengthFieldBasedFrameDecoder}
 * or {@link io.netty.handler.codec.LineBasedFrameDecoder}
 *
 * If a custom frame decoder is required, then one needs to be careful when implementing
 * on with ByteToMessageDecoder.
 *
 * Pitfalls:
 * Be aware that sub-class of ByteToMessageDecoder MUST NOT
 * annotated with {@link @Sharable}
 *
 * @className: ByteToMessageDecoder
 * @date July 25,2018
 */
public abstract class ByteToMessageDecoder extends ChannelInboundHandlerAdapter {


    /**
     * Cumulate {@link ByteBuf}s by merge them into one ByteBuf, using memory copies.
     */
    public static final Cumulator MERGE_CUMULATOR = new Cumulator() {
        @Override
        public ByteBuf cumulate(ByteBufAllocator alloc, ByteBuf cumulation, ByteBuf in) {
            ByteBuf buffer;
            if (cumulation.writerIndex() > cumulation.maxCapacity() - in.readableBytes()
                    || cumulation.refCnt() > 1) {

                buffer = null; // pseudo code
                // buffer = expandCumulation(alloc, cumulation, in.readableBytes())
            } else {
                buffer = cumulation;
            }
            buffer.writeBytes(in);
            in.release();
            return buffer;
        }
    };

    ByteBuf cumulation;
    private boolean first;
    private Cumulator cumulator = MERGE_CUMULATOR;

    @Override
    // 模版方法模式
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            // CodecOutputList out = CodecOutputList.newInstance();
            List<Object> out = new ArrayList<>();
            try {
                ByteBuf data = (ByteBuf) msg;
                first = cumulation == null;
                if (first) {
                    cumulation = data;
                } else {
                    cumulation = cumulator.cumulate(ctx.alloc(), cumulation, data);
                }
                callDecode(ctx, cumulation, out);
            } finally {
                if (cumulation != null && !cumulation.isReadable()) {
                    cumulation.release();
                    cumulation = null;
                }
                // .....
                int size = out.size();
                fireChannelRead(ctx, out, size);
            }
        } else {
            // 从当前ChannelHandlerContext节点开始向后传播事件
            ctx.fireChannelRead(msg);
        }
    }


    /**
     * Called once data should be decoded form the given {@link ByteBuf}.
     * This method will call {@link #decode(ChannelHandlerContext, ByteBuf, List)}
     * as long as decoding should take place.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link ByteToMessageDecoder} belongs to
     * @param in  the {@link ByteBuf} from which to read data.
     * @param out   the {@link List} to which decoded messages should be added
     */
    protected void callDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        try {

            // writerIndex - readerIndex > 0
            while (in.isReadable()) {
                int outSize = out.size();

                if (outSize > 0) {
                    fireChannelRead(ctx, out, outSize);
                    out.clear();

                    // Check if this handler was removed before continuing with decoding.
                    // If is was removed, it is not safe to continue to operate on the buffer.
                    if (ctx.isRemoved()) {
                        break;
                    }
                    outSize = 0;
                }

                int oldInputLength = in.readableBytes();
                // Hook method
                decode(ctx, in, out);

                if (ctx.isRemoved()) {
                    break;
                }

                if (outSize == out.size()) {
                    if (oldInputLength == in.readableBytes()) {
                        break;
                    } else {
                        continue;
                    }
                }

                if (oldInputLength == in.readableBytes()) {
                    throw new DecoderException(StringUtil.simpleClassName(getClass()) +
                            ".decode() did not read anything but decoded a message.");
                }

                /**
                 * if (isSingleDecode()) {
                 *     break;
                 * }
                 */

            }
        } catch (DecoderException e) {
            throw e;
        } catch (Throwable cause) {
            throw new DecoderException(cause);
        }
    }

    // Hook method，子类用来扩展
    /**
     * Decode from one {@link ByteBuf} to an other. This method will be called till either
     * the input {@link ByteBuf} has noting to read when return from this method or till nothing was
     * read from the input {@link ByteBuf}
     *
     * @param ctx  the {@link ChannelHandlerContext} which this {@link ByteToMessageDecoder} belongs to
     * @param in    the {@link ByteBuf} from which to read data
     * @param out   the {@link List} to which decoded messages should be added
     * @throws Exception is thrown if an error occur
     */
    protected abstract void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception;

    // Get {@code numElements} out of the List and
    // forward these through the pipeline.
    static void fireChannelRead(ChannelHandlerContext ctx, List<Object> msgs, int numElements) {
        for (int i = 0; i < numElements; i++) {
            ctx.fireChannelRead(msgs.get(i));
        }
    }

    /**
     * Cumulate {@link io.netty.buffer.ByteBuf}s.
     */
    public interface Cumulator {

        /**
         * Cumulate the given {@link ByteBuf}s and return the {@link ByteBuf} that holds
         * the cumulated bytes.
         * The implementation is responsible to correctly handle the life-cycle of the given
         * {@link ByteBuf}s and so call {@link ByteBuf#release()} if a {@link ByteBuf} is fully
         * consumed.
         */
        ByteBuf cumulate(ByteBufAllocator alloc, ByteBuf cumulation, ByteBuf in);
    }
}
