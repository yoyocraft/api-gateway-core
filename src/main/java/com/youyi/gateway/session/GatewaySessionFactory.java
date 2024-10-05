package com.youyi.gateway.session;

/**
 * 网关会话工厂
 * @author yoyocraft
 * @date 2024/10/05
 */
public interface GatewaySessionFactory {

    GatewaySession openSession() throws Exception;
}
