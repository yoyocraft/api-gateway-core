package com.youyi.gateway.session;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * 网关会话服务
 * @author yoyocraft
 * @date 2024/10/04
 */
public class SessionServer implements Callable<Channel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionServer.class);

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());

    private Channel channel;

    @Override
    public Channel call() throws Exception {
        ChannelFuture channelFuture = null;
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new SessionChannelInitializer());

            channelFuture = bootstrap.bind(new InetSocketAddress(7397)).syncUninterruptibly();
            this.channel = channelFuture.channel();
        } catch (Exception e) {
            LOGGER.error("Server socket start error.", e);
        } finally {
            if (Objects.nonNull(channelFuture) && channelFuture.isSuccess()) {
                LOGGER.info("Server socket start done.");
            } else {
                LOGGER.error("Server socket start error.");
            }
        }
        return channel;
    }
}
