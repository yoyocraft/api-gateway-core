package com.youyi.gateway.socket;

import com.youyi.gateway.session.GatewaySessionFactory;
import com.youyi.gateway.socket.handler.GatewayServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * 会话初始化器
 * @author yoyocraft
 * @date 2024/10/05
 */
public class GatewayChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final GatewaySessionFactory gatewaySessionFactory;

    public GatewayChannelInitializer(GatewaySessionFactory gatewaySessionFactory) {
        this.gatewaySessionFactory = gatewaySessionFactory;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        channel.pipeline()
                .addLast(new HttpRequestDecoder())
                .addLast(new HttpResponseEncoder())
                .addLast(new HttpObjectAggregator(1024 * 1024))
                .addLast(new GatewayServerHandler(gatewaySessionFactory));
    }
}
