package com.youyi.gateway.session.factory;

import com.youyi.gateway.session.Configuration;
import io.netty.channel.Channel;

import java.util.concurrent.Future;

/**
 * 会话工厂建造者
 * @author yoyocraft
 * @date 2024/10/05
 */
public class GenericReferenceSessionFactoryBuilder {

    public Future<Channel> build(Configuration configuration) {
        try {
            return new GenericReferenceSessionFactory(configuration).openSession();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
