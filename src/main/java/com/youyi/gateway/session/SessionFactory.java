package com.youyi.gateway.session;

import io.netty.channel.Channel;

import java.util.concurrent.Future;

/**
 * 会话工厂
 * @author yoyocraft
 * @date 2024/10/05
 */
public interface SessionFactory {

    /**
     * 创建会话
     * @return Channel Future
     */
    Future<Channel> openSession() throws Exception;
}
