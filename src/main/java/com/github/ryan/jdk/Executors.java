package com.github.ryan.jdk;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: Executors
 * @date July 16,2018
 */
public class Executors {


    @FunctionalInterface
    // The Runnable interface should be implemented by any class
    // whose instances are intended to be executed by a thread.
    interface Runnable {

        /**
         * When an object implementing interface Runnable is used
         * to create a thread, starting the thread causes the object's
         * run method to be called in that separately executing thread.
         */
        void run();
    }

    /**
     * The Callable interface is similar to Runnable, in that both are designed
     * for classes whose instances are potentially executed by another thread.
     * A Runnable, however, does not return a result and cannot throw a checked
     * exception
     *
     * @param <V> the result type of method call
     */
    @FunctionalInterface
    interface Callable<V> {

        // Compute a result, or throw an exception if unable to do so.
        V call() throws Exception;
    }

    // Returns a Callable object that, when called, runs the given task
    // and returns null
    public static Callable<Object> callable(Runnable task) {
        if (task == null) {
            throw new NullPointerException();
        }

        return new RunnableAdapter<Object>(task, null);
    }

    // A callable that runs given task and returns given result
    static final class RunnableAdapter<T> implements Callable<T> {
        final Runnable task;
        final T result;

        public RunnableAdapter(Runnable task, T result) {
            this.task = task;
            this.result = result;
        }

        @Override
        public T call() throws Exception {
            task.run();
            return result;
        }
    }

}
