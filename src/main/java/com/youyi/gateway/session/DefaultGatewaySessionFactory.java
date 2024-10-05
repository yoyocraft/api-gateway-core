package com.youyi.gateway.session;

/**
 * 默认网关会话工厂
 * @author yoyocraft
 * @date 2024/10/05
 */
public class DefaultGatewaySessionFactory implements GatewaySessionFactory {
    private final Configuration configuration;

    public DefaultGatewaySessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public GatewaySession openSession() throws Exception {
        return new DefaultGatewaySession(configuration);
    }
}
