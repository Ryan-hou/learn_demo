package com.github.ryan.jvm;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author ryan.houyl@gmail.com
 * @description
 * 类加载器与instanceof关键字演示
 * 比较两个类是否"相等"，只有在这两个类是同一个类加载器加载的前提下才有意义
 * 每一个类加载器，都有一个独立的类名称空间，任意一个类，需要由加载他的类加载器和这个类本身
 * 一同确定其在虚拟机中的唯一性
 *
 * @className ClassLoaderTest
 * @date September 07,2018
 */
@Slf4j
public class ClassLoaderTest {

    public static void main(String[] args) throws Exception {

        ClassLoader myLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {

                try {
                    String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";

//                     System.out.println(fileName);
//                     System.out.println(getClass().getName());
                    InputStream in = getClass().getResourceAsStream(fileName);
                    if (in == null) {
                        return super.loadClass(name);
                    } else {
                        byte[] b = new byte[in.available()];
                        in.read(b);
                        return defineClass(name, b, 0, b.length);
                    }
                } catch (IOException e) {
                    throw new ClassNotFoundException(name);
                }
            }
        };

        Class<?> clazz = myLoader.loadClass("com.github.ryan.jvm.ClassLoaderTest");
        // 先实例化父类再实例化子类（ClassLoaderTest -> Object）
        Object obj = clazz.newInstance();
        log.info("class name: {}", obj.getClass());

        // 返回false
        // 虚拟机存在两个 ClassLoaderTest 类，一个是由系统应用程序类加载器加载的，另一个是由我们自定义的类加载器加载的
        // 虽然都来自同一个Class文件，但依然是两个独立的类，做对象所属类型检查时结果自然为false
        log.info("same classLoader? {}", obj instanceof ClassLoaderTest);

        // com.github.ryan.jvm.ClassLoaderTest$1@xxxxxxxx
        log.info("Clazz Loader: {}", clazz.getClassLoader());
        // sun.misc.Launcher$AppClassLoader@xxxxxxxx
        log.info("Default Loader: {}", ClassLoaderTest.class.getClassLoader());

    }
}
