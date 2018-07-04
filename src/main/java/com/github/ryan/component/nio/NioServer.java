package com.github.ryan.component.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * @className: NioServer
 * @date July 03,2018
 */
@Slf4j
public class NioServer {

    public static void main(String[] args) {

        try {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress("127.0.0.1", 8000));
            // Newly-created selectable channels are always in blocking mode.
            // A channel must be placed into non-blocking mode before being registered with a selector
            ssc.configureBlocking(false);

            Selector selector = Selector.open();
            // 注册channel，并指定感兴趣的事件是 accept
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            ByteBuffer writeBuffer = ByteBuffer.allocate(128);
            writeBuffer.put("received".getBytes()).flip();

            while (true) {
                System.out.println("Selector waiting for channel events.......");
                // select() 方法会阻塞，直到至少有一个channel被selected
                int nReady = selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();

                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();

                    if (key.isAcceptable()) {
                        // 创建新的连接(channel)，并且把连接注册到selector上，并声明
                        // 这个channel只对读操作感兴趣
                        SocketChannel socketChannel = ssc.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        readBuffer.clear();
                        // channel -> buffer
                        socketChannel.read(readBuffer);
                        System.out.println("received: " + new String(readBuffer.array()));

                        key.interestOps(SelectionKey.OP_WRITE);
                    } else if (key.isWritable()) {
                        // writeBuffer.flip().rewind(); error！！！
                        // 注意flip！writeBuffer已经调用过一次flip方法，连续两次调用将导致writeBuffer不可写！
                        writeBuffer.rewind();

                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        // buffer -> channel("received")
                        socketChannel.write(writeBuffer);
                        key.interestOps(SelectionKey.OP_READ);
                    } else {
                        // ...
                    }
                }
            }

        } catch (IOException e) {
            log.error("nio server exception,", e);
        }
    }
}
