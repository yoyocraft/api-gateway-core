package com.youyi.gateway.socket;

import com.youyi.gateway.mapping.HttpCmdType;
import com.youyi.gateway.mapping.HttpStatement;
import com.youyi.gateway.session.Configuration;
import com.youyi.gateway.session.DefaultGatewaySessionFactory;
import com.youyi.gateway.session.GatewaySessionFactory;
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
 * @date 2024/10/05
 */
@RunWith(JUnit4.class)
public class GatewaySocketServerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewaySocketServerTest.class);

    @Test
    public void test_gatewaySocketServer() throws ExecutionException, InterruptedException {
        // 1. 创建配置信息，注册 MapperMethod
        Configuration configuration = new Configuration();
        configuration.addMapper(
                new HttpStatement(
                        "api-gateway-test",
                        "com.youyi.gateway.api.TestService",
                        "sayHi",
                        "/test/sayHi",
                        HttpCmdType.GET
                )
        );

        // 2. 创建会话工厂
        GatewaySessionFactory gatewaySessionFactory = new DefaultGatewaySessionFactory(configuration);

        // 3. 启动网关服务
        GatewaySocketServer gatewaySocketServer = new GatewaySocketServer(gatewaySessionFactory);

        Future<Channel> future = Executors.newFixedThreadPool(1).submit(gatewaySocketServer);

        Channel channel = future.get();
        if (Objects.isNull(channel)) {
            throw new RuntimeException("channel is null");
        }

        while (!channel.isActive()) {
            LOGGER.info("waiting for netty server start...");
            Thread.sleep(500);
        }

        LOGGER.info("netty server start success. Address: {}", channel.localAddress());
        Thread.sleep(Long.MAX_VALUE);
    }
}
