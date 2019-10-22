package com.github.ryan.component.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ryan.houyl@gmail.com
 * @description
 * @className BioServer
 * @date November 23,2018
 */
@Slf4j
public class BioServer {

    private static final ExecutorService executorService =
            new ThreadPoolExecutor(0,
                    Integer.MAX_VALUE,
                    60L,
                    TimeUnit.SECONDS,
                    new SynchronousQueue<>());

    private static class SocketThread implements Runnable {

        private Socket socket;

        public SocketThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // write -> native socketWrite0
                socket.getOutputStream().write("hello client, I'm server!".getBytes());

                // read -> native socketRead0 -> 阻塞主线程 -> 线程池
                byte[] content = new byte[256];
                int len = socket.getInputStream().read(content);
                System.out.println(new String(content, 0, len));

                // close
                socket.close();
            } catch (IOException e) {
                log.error("SocketThread - {} exception!", Thread.currentThread().getName(), e);
            }
        }
    }


    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket();
            // bind -> listen
            serverSocket.bind(new InetSocketAddress((InetAddress) null, 8888)); // contains listen
            // or directly use
            // ServerSocket serverSocket1 = new ServerSocket(8888);

            while (true) {
                // accept 去tcp全连接队列取连接，没有连接会阻塞
                Socket socket = serverSocket.accept();
                executorService.submit(new SocketThread(socket));
//                // write -> native socketWrite0
//                socket.getOutputStream().write("hello client, I'm server!".getBytes());
//
//                // read -> native socketRead0 -> 阻塞主线程 -> 线程池
//                byte[] content = new byte[256];
//                socket.getInputStream().read(content);
//                System.out.println(new String(content));
//
//                // close
//                socket.close();
            }

            // socket.close();

        } catch (IOException e) {
            log.error("Server exception!", e);
        } finally {

        }
    }
}
