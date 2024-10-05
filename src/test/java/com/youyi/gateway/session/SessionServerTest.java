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
        Configuration configuration = new Configuration();
        configuration.addGenericReference(
                "api-gateway-test",
                "com.youyi.gateway.api.TestService",
                "sayHi"
        );

        GenericReferenceSessionFactoryBuilder builder = new GenericReferenceSessionFactoryBuilder();
        Future<Channel> future = builder.build(configuration);

        LOGGER.info("\uD83D\uDE80\uD83D\uDE80\uD83D\uDE80[API-Gateway] start done {}", future.get().id());

        Thread.sleep(Long.MAX_VALUE);
    }
}
