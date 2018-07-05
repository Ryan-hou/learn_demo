package com.github.ryan.component.netty.server.discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author ryan.houyl@gmail.com
 * @description:
 * Discards any incoming data.
 * @className: DiscardServer
 * @date July 05,2018
 */
public class DiscardServer {

    private final int port;

    public DiscardServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {

        // NioEventLoopGroup is a multithreaded event loop that handles I/O operation
        // bossGroup: accepts an incoming connection
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // workGroup: handles the traffic of the accepted connection once the boss
        // accepts the connection and registers the accepted connection to the worker
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            // Helper class that sets up a server
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    // The handler specified here will always be evaluated by a newly
                    // accepted Channel.
                    // The ChannelInitializer is a special handler that is purposed to help
                    // a user configure a new Channel
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DiscardServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync();

            // Wait until the  server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shot down your server.
            f.channel().closeFuture().sync();
        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception {

        new DiscardServer(9090).run();
    }
}
