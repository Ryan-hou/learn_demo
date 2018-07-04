package com.github.ryan.component.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: NioClient
 * @date July 03,2018
 */
@Slf4j
public class NioClient {

    public static void main(String[] args) {
        new Thread(new NioClientThread("hello")).start();
        new Thread(new NioClientThread("world")).start();
    }

    private static class NioClientThread implements Runnable {

        private String word;

        public NioClientThread(String word) {
            this.word = word;
        }

        public void run() {
            SocketChannel socketChannel = null;
            try {
                socketChannel = SocketChannel.open();
                socketChannel.connect(new InetSocketAddress("127.0.0.1", 8000));

                ByteBuffer writeBuffer = ByteBuffer.allocate(128);
                ByteBuffer readBuffer = ByteBuffer.allocate(128);
                writeBuffer.put(word.getBytes()).flip();

                while (true) {
                    writeBuffer.rewind();
                    // buffer -> channel("hello")
                    socketChannel.write(writeBuffer);

                    readBuffer.clear();
                    // channel -> buffer("received")
                    socketChannel.read(readBuffer);
                    System.out.println(new String(readBuffer.array()));
                }

            } catch (IOException e) {
                log.error("nio client exception,", e);
            } finally {

                if (socketChannel != null) {
                    try {
                        socketChannel.close();
                    } catch (IOException e) {
                        log.error("socket channel close error!", e);
                    }
                }
            }
        }
    }

}
