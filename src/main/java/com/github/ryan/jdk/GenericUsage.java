package com.github.ryan.jdk;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: GenericUsage
 * @date July 31,2018
 */
public class GenericUsage {

    class A {

    }

    class B extends A {

    }

    class C extends B {

    }

    public void testGenericParameterType() {
        List<Object> a = new ArrayList<>();
        List<String> b = new ArrayList<>();
        List<?> c = new ArrayList<>();

        // a = b; // compile error! List<Object> is NOT supertype of List<String>
        c = b; // List<?> is supertype of List<String>
        c = a; // List<?> is supertype of List<Object>
    }

    public void testSuperExtends() {
        List<? extends List<? super B>> list = new ArrayList<>();
        List<List<B>> listB = new ArrayList<>();
        List<List<A>> listA = new ArrayList<>();
        List<List<C>> listC = new ArrayList<>();

        list = listB;
        list = listA;
        // list = listC; // compile error!
    }

}
