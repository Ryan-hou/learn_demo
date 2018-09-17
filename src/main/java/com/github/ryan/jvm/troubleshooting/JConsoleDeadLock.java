package com.github.ryan.jvm.troubleshooting;

/**
 * @author ryan.houyl@gmail.com
 * @description
 * @className JConsoleDeadLock
 * @date September 17,2018
 */
public class JConsoleDeadLock {

    private static class SyncAddRunnable implements Runnable {
        int a, b;

        public SyncAddRunnable(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public void run() {
            synchronized (Integer.valueOf(a)) {
                synchronized (Integer.valueOf(b)) {
                    System.out.println(a + b);
                }
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(new SyncAddRunnable(1, 2)).start();
            new Thread(new SyncAddRunnable(2, 1)).start();
        }
    }
}
