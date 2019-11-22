package com.eding.http.server;


import com.eding.scanner.ActionFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * @program:eding-cloud
 * @description:
 * @author:jiagang
 * @create 2019-11-22 10:17
 */
public class ServerExample {


    public void start(int port) throws Exception {

        EventLoopGroup boss = new NioEventLoopGroup(1, new DefaultThreadFactory("HttpServer-boss"));
        EventLoopGroup worker = new NioEventLoopGroup(0, new DefaultThreadFactory("HttpServer-work"));

        try {
            final ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HttpServerInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            Channel ch = bootstrap.bind(port).sync().channel();
            ActionFactory.newInstance().scanAction("com.eding.action");
            System.err.println("Open your web browser and navigate to http://127.0.0.1:" + port + '/');
            ch.closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }
}
