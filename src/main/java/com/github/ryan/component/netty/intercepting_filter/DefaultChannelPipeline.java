package com.github.ryan.component.netty.intercepting_filter;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.NoSuchElementException;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * The default ChannelPipeline implementation. It is usually created
 * by a Channel implementation when the Channel is created.
 * @className: DefaultChannelPipeline
 * @date July 18,2018
 */
@Slf4j
public class DefaultChannelPipeline implements ChannelPipeline {

    private static final String HEAD_NAME = generateName0(HeadContext.class);
    private static final String TAIL_NAME = generateName0(TailContext.class);

    final AbstractChannelHandlerContext head;
    final AbstractChannelHandlerContext tail;

    private final Channel channel;

    public DefaultChannelPipeline(Channel channel) {
        this.channel = ObjectUtil.checkNotNull(channel, "channel");

        head = new HeadContext(this);
        tail = new TailContext(this);

        head.next = tail;
        tail.prev = head;
    }

    // 从ChannelPipeline头节点开始向后传播，
    @Override
    public ChannelPipeline fireChannelRead(Object msg) {
        AbstractChannelHandlerContext.invokeChannelRead(head, msg);
        return this;
    }

    @Override
    public ChannelPipeline addFirst(String name, ChannelHandler handler) {
        // todo
        return null;
    }

    @Override
    public ChannelPipeline addLast(String name, ChannelHandler handler) {
        final AbstractChannelHandlerContext newCtx;
        synchronized (this) {
            // checkMultiplicity(handler)

            newCtx = newContext(filterName(name, handler), handler);
            addLast0(newCtx);
        }
        callHandlerAdded0(newCtx);
        return this;
    }

    // 双向链表尾插法，head/tail节点均已存在
    private void addLast0(AbstractChannelHandlerContext newCtx) {
        AbstractChannelHandlerContext prev = tail.prev;
        newCtx.prev = prev;
        newCtx.next = tail;
        prev.next = newCtx;
        tail.prev = newCtx;
    }

    private void callHandlerAdded0(final AbstractChannelHandlerContext ctx) {
        try {
            // 回调Handler接口实现类的handlerAdded回调方法
            ctx.handler().handlerAdded(ctx);
            ctx.setAddComplete();
        } catch (Exception e) {
            //todo
        }
    }

    @Override
    public ChannelHandler removeLast() {
        if (head.next == tail) {
            throw new NoSuchElementException();
        }
        return remove(tail.prev).handler();
    }

    private AbstractChannelHandlerContext remove(final AbstractChannelHandlerContext ctx) {
        assert ctx != head && ctx != tail;

        synchronized (this) {
            remove0(ctx);
        }
        // callHandlerRemoved0(ctx)
        return ctx;
    }

    // 双向链表删除节点，且该节点不为首尾节点
    private static void remove0(AbstractChannelHandlerContext ctx) {
        AbstractChannelHandlerContext prev = ctx.prev;
        AbstractChannelHandlerContext next = ctx.next;
        prev.next = next;
        next.prev = prev;
    }

    @Override
    public Channel channel() {
        return channel;
    }

    private AbstractChannelHandlerContext newContext(String name, ChannelHandler handler) {
        return new DefaultChannelHandlerContext(this, name, handler);
    }

    private static String generateName0(Class<?> handlerType) {
        return StringUtil.simpleClassName(handlerType) + "#0";
    }

    private String filterName(String name, ChannelHandler handler) {
        if (name == null) {
            return generateName(handler);
        }
        // checkDuplicateName(name);
        return name;
    }

    private String generateName(ChannelHandler handler) {
        Class<?> handlerType = handler.getClass();
        return generateName0(handlerType);
    }

    /**
     * Called once a message hit the end of the ChannelPipeline without been handled by the user
     * in ChannelInboundHandler#channelRead(ChannelHandlerContext, Object)
     */
    protected void onUnhandledInboundMessage(Object msg) {
        try {
            log.debug(
                 "Discard inbound message {} that reached at the tail of the pipeline." +
                    "Please check your pipeline configuration.", msg);
        } finally {
            // ReferenceCountUtil.release(msg);
        }
    }


    // A special catch-all handler that handles both bytes and messages.
    final class TailContext extends AbstractChannelHandlerContext implements ChannelHandler {

        public TailContext(DefaultChannelPipeline pipeline) {
            super(pipeline, TAIL_NAME);
            setAddComplete();
        }

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            // NOOP
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            onUnhandledInboundMessage(msg);
        }

        @Override
        public ChannelHandler handler() {
            return this;
        }
    }


    // netty中inbound事件从Head节点向Tail节点传播，outbound事件从Tail节点向Head节点传播
    final class HeadContext extends AbstractChannelHandlerContext implements ChannelHandler {

        HeadContext(DefaultChannelPipeline pipeline) {
            super(pipeline, HEAD_NAME);
            setAddComplete();
        }

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            // NOOP
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ctx.fireChannelRead(msg);
        }

        @Override
        public ChannelHandler handler() {
            return this;
        }
    }

}
