package com.github.ryan.component.netty.strategy_pattern;

import io.netty.util.concurrent.EventExecutor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * Default implementation which uses simple round-robin to
 * choose new EventExecutor.
 * @className: DefaultEventExecutorChooserFactory
 * @date July 25,2018
 */
public class DefaultEventExecutorChooserFactory implements EventExecutorChooserFactory {

    // -------------- 单例工厂(eager initialization) ----------------
    public static final DefaultEventExecutorChooserFactory INSTANCE = new DefaultEventExecutorChooserFactory();

    private DefaultEventExecutorChooserFactory() {}

    // -------------- 策略模式选择EventExecutorChooser实现类 -----------

    // 策略者
    @Override
    public EventExecutorChooser newChooser(EventExecutor[] executors) {
        if (isPowerOfTwo(executors.length)) {
            return new PowerOfTwoEventExecutorChooser(executors);
        } else {
            return new GenericEventExecutorChooser(executors);
        }
    }

    private static boolean isPowerOfTwo(int val) {
        assert val > 0;
        return (val & (val - 1)) == 0;
        // netty: return (val & -val) == val;
    }


    // 策略实现类
    private static final class PowerOfTwoEventExecutorChooser implements EventExecutorChooser {

        private final AtomicInteger idx = new AtomicInteger();
        private final EventExecutor[] executors;

        public PowerOfTwoEventExecutorChooser(EventExecutor[] executors) {
            this.executors = executors;
        }

        @Override
        public EventExecutor next() {
            // executors.length: 2的幂
            return executors[idx.getAndIncrement() & (executors.length - 1)];
        }
    }

    private static final class GenericEventExecutorChooser implements EventExecutorChooser {

        private final AtomicInteger idx = new AtomicInteger();
        private final EventExecutor[] executors;

        public GenericEventExecutorChooser(EventExecutor[] executors) {
            this.executors = executors;
        }

        @Override
        public EventExecutor next() {
            return executors[Math.abs(idx.getAndIncrement() % executors.length)];
        }
    }
}
