package com.github.ryan.jvm.troubleshooting;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author ryan.houyl@gmail.com
 * @description
 * @className JConsoleThread
 * @date September 17,2018
 */
public class JConsoleThread {

    // 线程死循环演示
    private static void createBusyThread() {
        Thread thread = new Thread(() -> {
            // 执行循环直到线程切换，耗费cpu资源
            while (true) {
                System.out.print("-");
            }
        }, "testBusyThread");
        thread.start();
    }


    // 线程锁等待演示
    private static void createLockThread(final Object lock) {
        Thread thread = new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                    // 线程处于WAITING状态，等待lock对象的notify或者notifyAll方法
                    // 在被唤醒前不会被分配执行时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "testLockThread");

        thread.start();
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        br.readLine();
        createBusyThread();
        br.readLine();
        Object obj = new Object();
        createLockThread(obj);
    }

}
