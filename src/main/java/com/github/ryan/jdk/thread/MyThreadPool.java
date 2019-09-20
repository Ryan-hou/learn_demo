package com.github.ryan.jdk.thread;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 简化的线程池，仅用来说明工作原理
 * 采用生产者 - 消费者模型：
 * 线程池的使用方是生产者，线程池本身是消费者
 */
public class MyThreadPool {

    // 利用阻塞队列实现生产者-消费者模型
    private BlockingQueue<Runnable> workQueue;
    // 保存内部工作线程
    private Set<WorkerThread> threads;

    public MyThreadPool(int poolSize, BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
        threads = new HashSet<>();
        for (int i = 0; i < poolSize; i++) {
            WorkerThread worker = new WorkerThread();
            worker.start();
            threads.add(worker);
        }
    }

    // 提交任务 - 生产者往阻塞队列放任务
    public void execute(Runnable command) {
        workQueue.offer(command);
    }

    // 工作线程负责消费任务，并执行任务
    private class WorkerThread extends Thread {
        @Override
        public void run() {
            // 循环取任务并执行
            while (true) {
                Runnable task = workQueue.poll(); // 队列为空时，线程阻塞
                task.run(); // 任务执行
            }
        }
    }

    public static void main(String[] args) {
        // usage demo
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(2);
        MyThreadPool pool = new MyThreadPool(10, workQueue);
        pool.execute(() -> System.out.println("thread pool executing!"));
    }
}
