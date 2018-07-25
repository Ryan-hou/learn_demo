package com.github.ryan.component.netty.strategy_pattern;

import io.netty.util.concurrent.EventExecutor;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * Factory that creates new EventExecutorChooser
 * 工厂模式
 *
 * @className: EventExecutorChooserFactory
 * @date July 25,2018
 */
public interface EventExecutorChooserFactory {

    // Returns a new EventExecutorChooser
    EventExecutorChooser newChooser(EventExecutor[] executors);

    /**
     * Chooses the next EventExecutor to use.
     * 策略接口
     */
    interface EventExecutorChooser {

        // Returns the new EventExecutor to use.
        EventExecutor next();
    }
}
