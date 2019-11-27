package com.eding.skelecton;

import com.eding.skelecton.http.server.HttpServerInitializer;
import com.eding.skelecton.action.ActionFactory;
import com.eding.skelecton.config.ServerConfigLoader;
import com.eding.skelecton.config.AppConfigLoader;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @program:http-service
 * @description:
 * @author:jiagang
 * @create 2019-11-25 17:59
 */
@Slf4j
public class AppRunner {

    static long  startTime=System.currentTimeMillis();

    public static void run() throws Exception {
        run(null);
    }

    public static void run(Class<?> primarySource) throws Exception {
        log.info("EDing-HTTPServer statring.....");
//        log.info(
//                "\n\033[41m       \033[0;39m \033[41m     \033[0;39m   \033[41m     \033[0;39m \033[41m \033[0;39m     \033[41m \033[0;39m  \033[41m     \033[0;39m     \033[41m \033[0;39m     \033[41m \033[0;39m \033[41m       \033[0;39m \033[41m       \033[0;39m \033[41m      \033[0;39m      \033[41m     \033[0;39m  \033[41m       \033[0;39m \033[41m      \033[0;39m  \033[41m \033[0;39m     \033[41m \033[0;39m \033[41m       \033[0;39m \033[41m      \033[0;39m  \n" +
//                "\033[41m \033[0;39m       \033[41m \033[0;39m     \033[41m \033[0;39m   \033[41m \033[0;39m   \033[41m  \033[0;39m    \033[41m \033[0;39m \033[41m \033[0;39m     \033[41m \033[0;39m    \033[41m \033[0;39m     \033[41m \033[0;39m    \033[41m \033[0;39m       \033[41m \033[0;39m    \033[41m \033[0;39m     \033[41m \033[0;39m    \033[41m \033[0;39m     \033[41m \033[0;39m \033[41m \033[0;39m       \033[41m \033[0;39m     \033[41m \033[0;39m \033[41m \033[0;39m     \033[41m \033[0;39m \033[41m \033[0;39m       \033[41m \033[0;39m     \033[41m \033[0;39m\n" +
//                "\033[41m \033[0;39m       \033[41m \033[0;39m     \033[41m \033[0;39m   \033[41m \033[0;39m   \033[41m \033[0;39m \033[41m \033[0;39m   \033[41m \033[0;39m \033[41m \033[0;39m          \033[41m \033[0;39m     \033[41m \033[0;39m    \033[41m \033[0;39m       \033[41m \033[0;39m    \033[41m \033[0;39m     \033[41m \033[0;39m    \033[41m \033[0;39m       \033[41m \033[0;39m       \033[41m \033[0;39m     \033[41m \033[0;39m \033[41m \033[0;39m     \033[41m \033[0;39m \033[41m \033[0;39m       \033[41m \033[0;39m     \033[41m \033[0;39m\n" +
//                "\033[41m      \033[0;39m  \033[41m \033[0;39m     \033[41m \033[0;39m   \033[41m \033[0;39m   \033[41m \033[0;39m  \033[41m \033[0;39m  \033[41m \033[0;39m \033[41m \033[0;39m  \033[41m    \033[0;39m    \033[41m       \033[0;39m    \033[41m \033[0;39m       \033[41m \033[0;39m    \033[41m      \033[0;39m      \033[41m     \033[0;39m  \033[41m     \033[0;39m   \033[41m      \033[0;39m  \033[41m \033[0;39m     \033[41m \033[0;39m \033[41m     \033[0;39m   \033[41m      \033[0;39m\n" +
//                "\033[41m \033[0;39m       \033[41m \033[0;39m     \033[41m \033[0;39m   \033[41m \033[0;39m   \033[41m \033[0;39m   \033[41m \033[0;39m \033[41m \033[0;39m \033[41m \033[0;39m     \033[41m \033[0;39m    \033[41m \033[0;39m     \033[41m \033[0;39m    \033[41m \033[0;39m       \033[41m \033[0;39m    \033[41m \033[0;39m                \033[41m \033[0;39m \033[41m \033[0;39m       \033[41m \033[0;39m   \033[41m \033[0;39m    \033[41m \033[0;39m   \033[41m \033[0;39m  \033[41m \033[0;39m       \033[41m \033[0;39m   \033[41m \033[0;39m\n" +
//                "\033[41m \033[0;39m       \033[41m \033[0;39m     \033[41m \033[0;39m   \033[41m \033[0;39m   \033[41m \033[0;39m    \033[41m  \033[0;39m \033[41m \033[0;39m     \033[41m \033[0;39m    \033[41m \033[0;39m     \033[41m \033[0;39m    \033[41m \033[0;39m       \033[41m \033[0;39m    \033[41m \033[0;39m          \033[41m \033[0;39m     \033[41m \033[0;39m \033[41m \033[0;39m       \033[41m \033[0;39m    \033[41m \033[0;39m    \033[41m \033[0;39m \033[41m \033[0;39m   \033[41m \033[0;39m       \033[41m \033[0;39m    \033[41m \033[0;39m\n" +
//                "\033[41m       \033[0;39m \033[41m     \033[0;39m   \033[41m     \033[0;39m \033[41m \033[0;39m     \033[41m \033[0;39m  \033[41m     \033[0;39m     \033[41m \033[0;39m     \033[41m \033[0;39m    \033[41m \033[0;39m       \033[41m \033[0;39m    \033[41m \033[0;39m           \033[41m     \033[0;39m  \033[41m       \033[0;39m \033[41m \033[0;39m     \033[41m \033[0;39m    \033[41m \033[0;39m    \033[41m       \033[0;39m \033[41m \033[0;39m     \033[41m \033[0;39m");
        AppConfigLoader configLoader = AppConfigLoader.getInstance();
        configLoader.loadConfig();
        ServerConfigLoader serverConfigLoader = configLoader.getServerConfigLoader();
        try {
            ActionFactory.newInstance().scanAction(serverConfigLoader.getActionPackage());
        } catch (Exception e) {
            throw new RuntimeException("扫描失败: " + serverConfigLoader.getActionPackage());
        }
        startHttpServer(serverConfigLoader.getServicePort());
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
            long endTime=System.currentTimeMillis();
            float excTime=(float)(endTime-startTime)/1000;
            log.info("EDing-HTTPServer started at port: "+port +" in "+excTime+" second");
            ch.closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
