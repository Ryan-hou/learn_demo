package com.github.ryan.jdk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ryan.houyl@gmail.com
 * @description
 * @className UnThreadSafeDemo
 * @date November 24,2018
 */
public class UnThreadSafeDemo {

    // Executors.newFixedThreadPool(4);
    private static final ExecutorService executorService =
            new ThreadPoolExecutor(4,
                    4,
                    0, TimeUnit.MILLISECONDS,
                    new LinkedBlockingDeque<>(),
                    new MyThreadFactory("UnThreadSafeDemoPool"));


    private static class MyThreadFactory implements ThreadFactory {

        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        MyThreadFactory(String threadPoolName) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                    threadPoolName +
                    "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }

    private static class Task implements Runnable {

        List<String> list;
        String content;

        public Task(List<String> list, String content) {
            this.list = list;
            this.content = content;
        }

        @Override
        public void run() {
            list.add(content);
        }
    }


    public static void main(String[] args) {
        List<String> list = new ArrayList<>(); // not thread safe
        // 线程名字：pool-1-thread-1, pool-1-thread-2,不具有可读性
        executorService.submit(new Task(list, "hello"));
        executorService.submit(new Task(list, "world"));
        System.out.println(list); // main thread,可能输出 "[]"
        executorService.shutdownNow();
    }
}
