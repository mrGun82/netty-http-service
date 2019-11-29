package com.eding.framework.http.server;



import com.eding.framework.exceptions.GlobalExceptionHandler;
import com.eding.framework.exceptions.HttpMethodNotSupportedException;
import com.eding.framework.exceptions.Result;
import com.eding.framework.http.request.EDRequest;
import com.eding.framework.action.ActionProxy;
import com.eding.framework.action.ParameterMaker;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

/**
 * @program:http-service
 * @description:
 * @author:jiagang
 * @create 2019-11-29 09:38
 */
@Slf4j
public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {

    private FullHttpRequest request;
    private Gson gson;

    public HttpServerInboundHandler() {
        gson = new GsonBuilder().disableHtmlEscaping().create();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof FullHttpRequest) {
                request = (FullHttpRequest) msg;
                HttpMethod method = request.method();
                Map<String, String> queryParmMap = Maps.newHashMap();

                if (HttpMethod.GET == method) {
                    QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
                    decoder.parameters().entrySet().forEach(entry -> {
                        queryParmMap.put(entry.getKey(), entry.getValue().get(0));
                    });
                } else if (HttpMethod.POST == method) {
                } else {
                    throw new HttpMethodNotSupportedException();
                }

                ByteBuf buf = request.content();
                EDRequest edRequest = gson.fromJson(buf.toString(CharsetUtil.UTF_8), EDRequest.class);
                ActionProxy proxy = new ActionProxy();
                ParameterMaker parameterMaker = new ParameterMaker();
                Map<String, Object> paramMap = edRequest.getParameter();

                if (Objects.nonNull(queryParmMap) && queryParmMap.size() > 0) {
                    paramMap.putAll(queryParmMap);
                }

                if (Objects.nonNull(paramMap)) {
                    for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                        parameterMaker.add(entry.getKey(), entry.getValue());
                    }
                }

                String result = proxy.doRequest(edRequest.getAction(), parameterMaker.toOgnl());
                writeResult(ctx, result, HttpResponseStatus.OK);
                return;
            } else {
                throw new HttpMethodNotSupportedException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Result result = new GlobalExceptionHandler().exceptionHandler(e);
            writeResult(ctx, result, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        } finally {
            request.release();
        }
    }

    private void writeResult(ChannelHandlerContext ctx, Result result, HttpResponseStatus status) {
        String resultString = gson.toJson(result);
        writeResult(ctx, resultString, status);
    }

    private void writeResult(ChannelHandlerContext ctx, String result, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(result, CharsetUtil.UTF_8));
        response.headers()
                .set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                .set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        ctx
                .writeAndFlush(response)
                .addListener(ChannelFutureListener.CLOSE);
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        log.info("连接的客户端地址:" + ctx.channel().remoteAddress());
//        super.channelActive(ctx);
//    }
}
