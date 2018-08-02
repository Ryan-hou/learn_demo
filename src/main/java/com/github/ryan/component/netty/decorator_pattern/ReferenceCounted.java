package com.github.ryan.component.netty.decorator_pattern;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * A reference-counted object that requires explicit deallocation.
 *
 * When a new ReferenceCounted is instantiated, it starts with the reference count of 1.
 * {@link #retain()} increases the reference count, and {@link #release()} decreases the
 * reference count. If the reference count it decreased to {@code 0}, the object will
 * be deallocated explicitly, and accessing the deallocated object will usually result in
 * an access violation.
 *
 * If an object that implements {@link ReferenceCounted} is a container of other objects that
 * implement {@link ReferenceCounted}, the contained objects will also be released via
 * {@link #release()} when the container's reference count becomes 0.
 *
 * @className: ReferenceCounted
 * @date August 02,2018
 */
public interface ReferenceCounted {

    /**
     * Returns the reference count of this object. If {@code 0}, it means this object
     * has been deallocated.
     */
    int refCnt();

    /**
     * Increases the reference count by {@code 1}.
     */
    ReferenceCounted retain();

    /**
     * Increase the reference count by the specified {@code increment}.
     */
    ReferenceCounted retain(int increment);

    /**
     * Decreases the reference count by {@code 1} and deallocates this object if the reference count
     * reaches at {@code 0}.
     *
     * @return {@code true} if and only if the reference count became {@code 0} and this object has
     * been deallocated
     */
    boolean release();

    /**
     * Decreases the reference count by the specified {@code decrement} and deallocates this object if
     * the reference count reaches at {@code 0}.
     *
     * @return {@code true} if and only if the reference count became {@code 0} and this object
     * has been deallocated.
     */
    boolean release(int decrement);


}
