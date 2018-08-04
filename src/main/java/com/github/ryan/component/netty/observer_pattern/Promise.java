package com.github.ryan.component.netty.observer_pattern;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * Special {@link Future} which is writable.
 * 实现的Future子类的状态是不可变的，如果我们想要变化，那该怎么办呢？使用 Promise
 * Promise从Uncompleted-->Completed的状态转变有且只能有一次，也就是说setSuccess和setFailure方法最多只会成功一个，
 * 此外，在setSuccess和setFailure方法中会通知注册到其上的监听者
 *
 * @className: Promise
 * @date July 31,2018
 */
public interface Promise<V> extends Future<V> {

    /**
     * Marks this future as a success and notifies all
     * listeners.
     *
     * If it is success or failed already it will throw an
     * {@link IllegalStateException}.
     */
    Promise<V> setSuccess(V result);

    /**
     * Marks this future as a success and notifies all listeners.
     *
     * @return {@code true} if and only if successfully marked this future
     *          as a success. Otherwise {@code false} because this
     *          future is already marked as either success or a failure.
     */
    boolean trySuccess(V result);

    Promise<V> setFailure(Throwable cause);

    boolean tryFailure(Throwable cause);

    @Override
    Promise<V> sync() throws InterruptedException;

    @Override
    Promise<V> syncUninterruptibly();

    @Override
    Promise<V> addListener(GenericFutureListener<? extends Future<? super V>> listener);

    @Override
    Promise<V> removeListener(GenericFutureListener<? extends Future<? super V>> listener);
}
