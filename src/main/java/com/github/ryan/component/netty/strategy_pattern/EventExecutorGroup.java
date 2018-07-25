package com.github.ryan.component.netty.strategy_pattern;

import io.netty.util.concurrent.EventExecutor;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: EventExecutorGroup
 * @date July 25,2018
 */
public interface EventExecutorGroup {

    /**
     * Returns one of the {@link io.netty.util.concurrent.EventExecutor}
     * managed by this EventExecutorGroup
     */
    EventExecutor next();
}
