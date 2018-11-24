package com.github.ryan.component.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author ryan.houyl@gmail.com
 * @description
 * @className BioClient
 * @date November 23,2018
 */
@Slf4j
public class BioClient1 {

    public static void main(String[] args) {
        try {
            // connect
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("localhost", 8888));
            // or directly use Socket socket = new Socket("localhost", 8888);

            // read
            byte[] content = new byte[256];
            socket.getInputStream().read(content);
            System.out.println(new String(content));

            // write
            socket.getOutputStream().write("Hello server, I'm Client1!".getBytes());
            // close
            socket.close();
        } catch (IOException e) {
            log.error("Client1 exception!", e);
        }
    }
}
