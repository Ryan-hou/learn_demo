package com.github.ryan.jdk.thread;

/**
 * @author ryan.houyl@gmail.com
 * @description
 * @className ThreadLocalDemo
 * @date September 11,2018
 */
public class ThreadLocalDemo {

    private static final ThreadLocal<Integer> aSum = ThreadLocal.withInitial(() -> 0);
    private static final ThreadLocal<Integer> bSum = ThreadLocal.withInitial(() -> 1);

    private static class CountThread implements Runnable {

        String name;

        public CountThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                aSum.set(aSum.get() + i);
                bSum.set(bSum.get() + i);
            }
            System.out.println("thread " + name + ", a count = " + aSum.get());
            System.out.println("thread " + name + ", b count = " + bSum.get());
        }
    }


    public static void main(String[] args) {

        for (int i = 0; i < 5; i ++) {
            new Thread(new CountThread(i + "")).start();
            // new Thread(new CountThread(i + "")).run(); // careful!
        }
    }

}
