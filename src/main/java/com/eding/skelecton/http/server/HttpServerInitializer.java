package com.eding.skelecton.http.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @program:eding-cloud
 * @description:
 * @author:jiagang
 * @create 2019-11-22 11:50
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline
                .addLast(new HttpServerCodec())
                .addLast(new HttpObjectAggregator(65535))
//                .addLast(new HttpResponseEncoder())
                .addLast(new HttpRequestDecoder())
                .addLast(new HttpServerInboundHandler());
    }
}