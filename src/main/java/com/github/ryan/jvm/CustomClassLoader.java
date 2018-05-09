package com.github.ryan.jvm;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: CustomClassLoader
 * @date May 09,2018
 */
@Slf4j
public class CustomClassLoader extends ClassLoader {

    private final String CLASSLOADER_NAME;
    private final String PATH = "/Users/Ryan-hou/Documents/test_staff/";
    private final String FILE_TYPE = ".class";

    public CustomClassLoader(String classLoaderName) {
        super();
        this.CLASSLOADER_NAME = classLoaderName;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] data = loadClassData(name);
        if (data == null) {
            throw new ClassNotFoundException(name);
        }

        return super.defineClass(name, data, 0, data.length);
    }

    private byte[] loadClassData(String name) {
        InputStream in = null;
        ByteArrayOutputStream baos = null;

        String filePath = PATH + name + FILE_TYPE;
        try {
            in = new FileInputStream(new File(filePath));
            baos = new ByteArrayOutputStream();
            int index;
            while (-1 != (index = in.read())) {
                baos.write(index);
            }
            byte[] data = baos.toByteArray();
            return data;
        } catch (FileNotFoundException e) {
            log.error("File not found error, file path = {}", filePath, e);
        } catch (IOException e) {
            log.error("read file error! ", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
               log.error("close io error! ", e);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "CustomClassLoader{" +
                "CLASSLOADER_NAME='" + CLASSLOADER_NAME + '\'' +
                '}';
    }

    public static void main(String[] args) throws Exception {
        CustomClassLoader loader = new CustomClassLoader("MyClassLoader");
        Class<?> clazz = loader.loadClass("Sample4ClassLoader");
        clazz.newInstance();
    }
}
