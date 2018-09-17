package com.github.ryan.jvm.troubleshooting;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ryan.houyl@gmail.com
 * @description
 * @className JConsoleMemory
 * @date September 17,2018
 */
public class JConsoleMemory {

    // 内存占位符对象，一个OOMObject对象大约占64KB
    private static class OOMObject {
        byte[] placeholder = new byte[64 * 1024];
    }

    private static void fillHeap(int num) throws InterruptedException {
        List<OOMObject> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            // 稍微延时，使监控曲线变化更加明显
            Thread.sleep(50);
            list.add(new OOMObject());
        }

        System.gc(); // Just for showcase, don't use like this
    }

    public static void main(String[] args) throws Exception {
        fillHeap(1000);
    }
}
