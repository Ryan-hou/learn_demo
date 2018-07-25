package com.github.ryan.component.netty.strategy_pattern;

import io.netty.util.concurrent.EventExecutor;

import java.util.concurrent.Executor;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: MultithreadEventExecutorGroup
 * @date July 25,2018
 */
public class MultithreadEventExecutorGroup implements EventExecutorGroup {

    private final EventExecutor[] children;
    private final EventExecutorChooserFactory.EventExecutorChooser chooser;

    public MultithreadEventExecutorGroup(int nThreads, Executor executor) {
        // nThread: the number of threads that will be used by this instance
        if (nThreads < 0) {
            throw new IllegalArgumentException(String.format("nThreads: %d (expected: > 0)", nThreads));
        }

        // executor: The Executor to use, or null if the default should be used.
//        if (executor == null) {
//            executor = new ThreadPerTaskExecutor(newDefaultThreadFactory());
//        }

        children = new EventExecutor[nThreads];

        for (int i = 0; i < nThreads; i++) {
            // children[i] = newChild(executor, args);
        }

        EventExecutorChooserFactory chooserFactory = DefaultEventExecutorChooserFactory.INSTANCE;
        chooser = chooserFactory.newChooser(children);
    }

    @Override
    public EventExecutor next() {
        return chooser.next();
    }
}
