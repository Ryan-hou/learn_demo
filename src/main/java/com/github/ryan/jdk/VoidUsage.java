package com.github.ryan.jdk;

import java.util.concurrent.Callable;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: VoidUsage
 * @date July 31,2018
 */
public class VoidUsage {

    // Void class:
    // Some time we need to represent the void keyword as an object
    /**
     *  The Void class is an uninstantiable placeholder class to hold a
     *  reference to the {@code Class} object representing the Java keyword
     *  void.
     */

    public static void testVoid() {
    }

    // You can create instance of Void using reflection, but they are not useful for
    // anything. Void is a way to indicate a generic method returns nothing.
    Callable<Void> callable = new Callable<Void>() {
        @Override
        public Void call() throws Exception {
            // do something
            return null;
        }
    };

    public static void main(String[] args) throws Exception {
        Class<?> returnType = VoidUsage.class.getMethod("testVoid", null).getReturnType();
        System.out.println(returnType); // void

        System.out.println(Void.class); // class java.lang.Void
        System.out.println("returnType == Void.class? " + (returnType == Void.class)); // false

        System.out.println("returnType == void.class? " + (returnType == void.class)); // true
        System.out.println("returnType == Void.TYPE? " + (returnType == Void.TYPE)); // true
    }
}
