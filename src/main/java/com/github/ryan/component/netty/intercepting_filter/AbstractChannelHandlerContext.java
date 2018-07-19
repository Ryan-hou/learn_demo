package com.github.ryan.component.netty.intercepting_filter;

import io.netty.util.internal.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * (链表元素)
 *
 * @className: AbstractChannelHandlerContext
 * @date July 18,2018
 */
@Slf4j
public abstract class AbstractChannelHandlerContext implements ChannelHandlerContext {

    volatile AbstractChannelHandlerContext prev;
    volatile AbstractChannelHandlerContext next;

    private static final AtomicIntegerFieldUpdater<AbstractChannelHandlerContext> HANDLER_STATE_UPDATER
            = AtomicIntegerFieldUpdater.newUpdater(AbstractChannelHandlerContext.class, "handlerState");

    private final DefaultChannelPipeline pipeline;
    private final String name;

    private volatile int handlerState = INIT;

    private static final int INIT = 0;
    // ChannelHandler#handlerAdded(ChannelHandlerContext) was called.
    private static final int ADD_COMPLETE = 2;

    public AbstractChannelHandlerContext(DefaultChannelPipeline pipeline, String name) {
        this.name = ObjectUtil.checkNotNull(name, "name");
        this.pipeline = pipeline;
    }

    @Override
    public Channel channel() {
        return pipeline.channel();
    }

    @Override
    public ChannelPipeline pipeline() {
        return pipeline;
    }

    @Override
    public String name() {
        return name;
    }

    // 从当前context节点的下一个节点开始向后传播
    // 责任链事件传播核心方法
    @Override
    public ChannelHandlerContext fireChannelRead(final Object msg) {
        invokeChannelRead(findContextInbound(), msg);
        return this;
    }

    private AbstractChannelHandlerContext findContextInbound() {
        AbstractChannelHandlerContext ctx = this;
//        do {
//           ctx = ctx.next;
//        } while (!ctx.inbound);
        return ctx.next;
    }

    static void invokeChannelRead(final AbstractChannelHandlerContext next, Object msg) {
//        EventExecutor executor = next.executor();
//        if (executor.inEventLoop()) {
//            next.invokeChannelRead(msg);
//        } else {
//            executor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    next.invokeChannelRead(msg);
//                }
//            });
//        }
        next.invokeChannelRead(msg);
    }

    private void invokeChannelRead(Object msg) {
        if (invokeHandler()) {
            try {
                handler().channelRead(this, msg);
            } catch (Exception t) {
                // notifyHandlerException(t);
                log.error("invokeChannelRead exception，", t);
            }
        } else {
            fireChannelRead(msg);
        }
    }

    // CASed handlerState
    final void setAddComplete() {
        for (;;) {
            int oldState = handlerState;
            if (HANDLER_STATE_UPDATER.compareAndSet(this, oldState, ADD_COMPLETE)) {
                return;
            }
        }
    }

    /**
     * Makes best possible effort to detect if ChannelHandler#handlerAdd(ChannelHandlerContext) was called
     * yet. If not return false and if called or could not detect return true.
     *
     */
    private boolean invokeHandler() {
        // Store in local variable to reduce volatile reads.
//        int handlerState = this.handlerState;
//        return handlerState == ADD_COMPLETE || (!ordered && handlerState == ADD_PENDING);
        return handlerState == ADD_COMPLETE;
    }
}
