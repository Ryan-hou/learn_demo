package com.github.ryan.component.netty.observer_pattern;

import java.util.EventListener;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * 观察者接口，被观察者为异步操作结果Future
 *
 * Listens to the result of a {@link Future}.
 * The result of the asynchronous operation is notified once this listener
 * is added by calling {@link Future#addListener(GenericFutureListener)}
 *
 * @className: GenericFutureListener
 * @date July 31,2018
 */
public interface GenericFutureListener<F extends Future<?>> extends EventListener {

    // 回调方法（通知观察者）
    /**
     * Invoked when the operation associated with the {@link Future} has been completed.
     *
     * @param future the source {@link Future} which called this callback
     */
    void operationComplete(F future) throws Exception;
}
