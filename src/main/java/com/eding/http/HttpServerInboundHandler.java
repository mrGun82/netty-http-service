package com.eding.http;

import com.eding.exceptions.GlobalExceptionHandler;
import com.eding.exceptions.Result;
import com.eding.http.request.Request;
import com.eding.scanner.ActionProxy;
import com.eding.scanner.ParameterMaker;
import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Objects;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @program:eding-cloud
 * @description:
 * @author:jiagang
 * @create 2019-11-22 10:19
 */
public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter {

    private HttpRequest httpRequest;
    private Gson gson;

    public HttpServerInboundHandler() {
        gson = new GsonBuilder().disableHtmlEscaping().create();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof HttpRequest) {
                httpRequest = (HttpRequest) msg;
                String uri = httpRequest.uri();
            }
            if (msg instanceof HttpContent) {
                HttpContent content = (HttpContent) msg;
                ByteBuf buf = content.content();
                Request request = gson.fromJson(buf.toString(CharsetUtil.UTF_8), Request.class);
                ActionProxy proxy = new ActionProxy();
                buf.release();
                ParameterMaker parameterMaker = new ParameterMaker();
                Map<String, Object> ParamMap = request.getParameter();
                if (Objects.nonNull(request.getParameter())) {
                    for (Map.Entry<String, Object> entry : ParamMap.entrySet()) {
                        System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                        parameterMaker.add(entry.getKey(), entry.getValue());
                    }
                }
                String result = proxy.doRequest(request.getAction(), parameterMaker.toOgnl());
                writeResult(ctx, result);
            }
            if(msg instanceof FullHttpRequest){
                FullHttpRequest req = (FullHttpRequest)msg;
// 1.获取URI
                String uri = req.uri();

                // 2.获取请求体
                ByteBuf buf = req.content();
                String content = buf.toString(CharsetUtil.UTF_8);

                // 3.获取请求方法
                HttpMethod method = req.method();

                // 4.获取请求头
                HttpHeaders headers = req.headers();


            }
        } catch (Exception e) {
            e.printStackTrace();
            Result result = new GlobalExceptionHandler().exceptionHandler(e);
            writeResult(ctx, result);
        }
    }

    private void writeResult(ChannelHandlerContext ctx, Result result) throws UnsupportedEncodingException {
        String resultString = gson.toJson(result);
        writeResult(ctx, resultString);
    }

    private void writeResult(ChannelHandlerContext ctx, String result) throws UnsupportedEncodingException {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(result.getBytes(Charsets.UTF_8)));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        if (HttpUtil.isKeepAlive(httpRequest)) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.write(response);
        ctx.flush();
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

}
