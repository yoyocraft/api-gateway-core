package com.youyi.gateway.session;

import com.youyi.gateway.session.handler.SessionServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * 会话初始化器
 * @author yoyocraft
 * @date 2024/10/04
 */
public class SessionChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        channel.pipeline()
                .addLast(new HttpRequestDecoder())
                .addLast(new HttpResponseEncoder())
                .addLast(new HttpObjectAggregator(1024 * 1024))
                .addLast(new SessionServerHandler());
    }
}
