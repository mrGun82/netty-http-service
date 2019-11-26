package com.eding.skelecton;

import com.eding.http.server.HttpServerInitializer;
import com.eding.skelecton.action.ActionFactory;
import com.eding.skelecton.config.ActionConfigLoader;
import com.eding.skelecton.config.AppConfig;
import com.eding.skelecton.config.AppConfigLoader;
import com.eding.skelecton.config.ConfigLoader;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.Objects;

/**
 * @program:http-service
 * @description:
 * @author:jiagang
 * @create 2019-11-25 17:59
 */
public class AppRunner {

    public static void run(Class<?> primarySource) throws Exception {
        AppConfigLoader configLoader = AppConfigLoader.getInstance();
        configLoader.loadConfig();
        ActionConfigLoader actionConfigLoader = configLoader.getActionConfigLoader();
        try {
            ActionFactory.newInstance().scanAction(actionConfigLoader.getActionPackage());
        } catch (Exception e) {
            throw new RuntimeException("扫描失败: " + actionConfigLoader.getActionPackage());
        }
        startHttpServer(actionConfigLoader.getServicePort());
    }

    private static void startHttpServer(Integer port) throws Exception {
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
            ch.closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
