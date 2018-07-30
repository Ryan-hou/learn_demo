package com.github.ryan.component.netty.iterate_pattern;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * Provides a mechanism to iterate over a collection of bytes.
 *
 * @className: ByteProcessor
 * @date July 27,2018
 */
@FunctionalInterface
public interface ByteProcessor {

    /**
     * @return {@code true} if the processor wants to continue the loop and
     * handle the next byte in the buffer.
     *      {@code false} if the processor wants to stop handling bytes and abort the loop.
     */
    boolean process(byte value) throws Exception;
}
