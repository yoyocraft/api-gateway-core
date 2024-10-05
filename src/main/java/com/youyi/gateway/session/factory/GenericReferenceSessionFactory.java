package com.youyi.gateway.session.factory;

import com.youyi.gateway.session.Configuration;
import com.youyi.gateway.session.SessionServer;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 泛化服务会话工厂
 * @author yoyocraft
 * @date 2024/10/05
 */
public class GenericReferenceSessionFactory implements SessionFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericReferenceSessionFactory.class);

    private final Configuration configuration;

    public GenericReferenceSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Future<Channel> openSession() throws Exception {
        SessionServer sessionServer = new SessionServer(configuration);
        Future<Channel> future = Executors.newFixedThreadPool(1).submit(sessionServer);

        Channel channel = future.get();
        if (Objects.isNull(channel)) {
            throw new RuntimeException("channel is null");
        }

        while (!channel.isActive()) {
            LOGGER.info("waiting for netty server start...");
            Thread.sleep(500);
        }

        LOGGER.info("netty server start success. Address: {}", channel.localAddress());
        return future;
    }
}
