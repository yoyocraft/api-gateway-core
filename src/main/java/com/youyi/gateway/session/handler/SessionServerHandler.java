package com.youyi.gateway.session.handler;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.youyi.gateway.bind.GenericReference;
import com.youyi.gateway.session.Configuration;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 会话服务处理器
 * @author yoyocraft
 * @date 2024/10/04
 */
public class SessionServerHandler extends BaseHandler<FullHttpRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionServerHandler.class);

    private final Configuration configuration;

    public SessionServerHandler(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void session(ChannelHandlerContext ctx, Channel channel, FullHttpRequest req) throws Exception {
        LOGGER.info("gateway receive request, uri: {}, method: {}", req.uri(), req.method());

        // 过滤掉 favicon.ico 请求
        String methodName = req.uri().substring(1);
        if ("favicon.ico".equals(methodName)) {
            return;
        }

        // 处理返回信息
        DefaultFullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

        // 泛化服务调用
        // FIXME 先写死 sayHi
        GenericReference genericReference = configuration.getGenericReference("sayHi");
        String ret = genericReference.$invoke("yoyocraft");
        ret += " " + System.currentTimeMillis();

        // 控制返回信息
        resp.content().writeBytes(JSON.toJSONBytes(ret, JSONWriter.Feature.PrettyFormat));

        // 设置头部信息
        resp.headers()
                .add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + "; charset=UTF-8")
                .add(HttpHeaderNames.CONTENT_LENGTH, resp.content().readableBytes())
                .add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                // cors
                .add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                .add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "*")
                .add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,PUT,DELETE,OPTION,HEAD")
                .add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");

        channel.writeAndFlush(resp);
    }
}

