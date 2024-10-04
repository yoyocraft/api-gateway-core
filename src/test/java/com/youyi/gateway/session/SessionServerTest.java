package com.youyi.gateway.session;

import io.netty.channel.Channel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author yoyocraft
 * @date 2024/10/04
 */
@RunWith(JUnit4.class)
public class SessionServerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionServerTest.class);

    @Test
    public void test_sessionServer() throws ExecutionException, InterruptedException {
        SessionServer sessionServer = new SessionServer();
        Future<Channel> future = Executors.newFixedThreadPool(2).submit(sessionServer);

        Channel channel = future.get();
        if (Objects.isNull(channel)) {
            LOGGER.error("netty server start error, channel is null");
            return;
        }

        while (!channel.isActive()) {
            LOGGER.info("waiting for netty server start...");
            Thread.sleep(500);
        }

        LOGGER.info("netty server start success.");
        Thread.sleep(Long.MAX_VALUE);
    }
}
