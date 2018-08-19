package com.github.ryan.component.netty.time;

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
 * @className: TimeServer
 * @date July 06,2018
 */
public class TimeServer {

    private final int port;

    public TimeServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        // 使用多Reactor多线程模式
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // mainReactor
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // subReactor
        // 耗时操作业务线程池
        // EventExecutorGroup threadPool = ...

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            /**
                             * Netty的默认方法，也就是说不在addLast(Handler)方法中指定线程池，
                             * 那么将使用默认的subReactor 即worker线程池 也即IO线程池 执行处理器中的业务逻辑代码
                             *
                             */
                            // outbound events: write -> Tail -> TimeEncoder -> Head
                            // inbound events: read -> Head -> TimeServerHandler -> Tail
                            ch.pipeline().addLast(new TimeEncoder(), new TimeServerHandler());

                            // 最佳实践
                            // 简单快速的业务逻辑可由IO线程池执行，复杂耗时的业务（如查询数据库，取得网络连接等）使用新的业务逻辑线程池执行
                            // ch.pipeline().addLast(threadPool, new TimeEncoder(), new TimeServerHandler());
                            // or
                            //ch.pipeline().addLast(new TimeEncoder(), new TimeServerHandler());
                            //ch.pipeline().addLast(threadPool, new ComputeWithSqlHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections
            ChannelFuture f = b.bind(port).sync(); // Waits for this future until it is done
            // 准确的说，这个方法在这里使用是有问题的，sync()完成后只能表明绑定事件运行完毕，但并不能说明绑定成功，虽然失败的可能性微乎其微


            // Wait util the server socket is closed.
            // 该方法仅仅是为了使当前main线程阻塞而不立即执行之后的各种shutdown()方法，
            // 其语义是等到服务端接受客户端连接的Channel被关闭时，才执行后面代码的操作
            // 在实际应用中，这样的代码并不实用，我们可能需要接受诸如kill命令后，优雅关闭线程组
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {

        new TimeServer(9090).run();
    }
}
