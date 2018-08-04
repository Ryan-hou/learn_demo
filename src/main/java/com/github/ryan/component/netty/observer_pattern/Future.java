package com.github.ryan.component.netty.observer_pattern;

import java.util.concurrent.TimeUnit;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * The result of an asynchronous operation.
 * 解决了jdk Future接口存在的两个问题：
 * 1）jdk Future中isDone()方法返回true表示：normal termination, an exception, or cancellation，
 * 实际使用中需要能够区分出不同的情况(Completed successfully / Completed with failure / Completed by cancellation)
 * 2）对于一个异步操作，我们更关心的是这个异步操作触发或者结束后能否再执行一系列动作（回调）
 * 需要给异步返回结果增加回调(Listener)事件，这样更加符合异步的操作
 *
 * @className: Future
 * @date July 31,2018
 */
@SuppressWarnings("ClassNameSameAsAncestorName")
public interface Future<V> extends java.util.concurrent.Future<V> {

    /**
     * Returns {@code true} if and only if the I/O operation was
     * completed successfully.
     */
    boolean isSuccess();

    /**
     * Returns the cause of the failed I/O operation if the I/O
     * operation has failed.
     *
     * @return the cause of the failure.
     *      {@code null} if succeed or this future is not
     *      completed yet.
     */
    Throwable cause();

    // boolean isCancelled(); // 父类接口中有提供


    /**
     * Returns the result without blocking. If the future is not done yet this will
     * return {@code null}.
     *
     * As it is possible that a {@code null} value is used to mark the future as
     * successful you also need to check if the future is really done with
     * {@link #isDone()} and not relay on the returned {@code null} value.
     */
    V getNow();

    /**
     *  Waits for this future until it is done, and rethrows the cause of the failure if
     *  this future failed.
     *  阻塞直到异步操作完成，失败时抛出失败原因(区别await方法的地方)
     */
    Future<V> sync() throws InterruptedException;

    Future<V> syncUninterruptibly();

    /**
     * Waits for this future to be completed.
     *
     * @throws InterruptedException
     *      if the current thread was interrupted
     */
    Future<V> await() throws InterruptedException;

    Future<V> awaitUninterruptibly();

    /**
     * Waits for this future to be completed within the
     * specified time limit.
     *
     * @return {@code true} if and only if the future was completed
     *      within the specified time limit.
     *
     * @throws InterruptedException
     *      if the current thread was interrupted
     */
    boolean await(long timeout, TimeUnit unit) throws InterruptedException;

    /**
     * Adds the specified listener to this future. The
     * specified listener is notified when this future is
     * {@linkplain #isDone() done}. If this future is already
     * completed, the specified listener is notified immediately.
     */
    // <? extends Future<? super V>> equals: Future<V> or Future<X>, X is supertype of V

    // Future<A> a = ...; a.addListener(new GenericFutureListener<Future<B>> {....} // listener); // B is superclass of A
    // callback: listener.operationComplete(a); // B is supertype of A,B有的方法，a一定有
    Future<V> addListener(GenericFutureListener<? extends Future<? super V>> listener);

    /**
     * Removes the first occurrence for each of the listeners from this future.
     * The specified listener is no longer notified when this
     * future is {@linkplain #isDone() done}. If the specified
     * listener is not associated with this future, this method
     * does nothing and returns silently.
     */
    Future<V> removeListener(GenericFutureListener<? extends Future<? super V>> listener);

}
