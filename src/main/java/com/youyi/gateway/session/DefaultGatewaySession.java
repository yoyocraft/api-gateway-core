package com.youyi.gateway.session;

import com.youyi.gateway.bind.GenericReference;
import com.youyi.gateway.datasource.Connection;
import com.youyi.gateway.datasource.DataSource;
import com.youyi.gateway.mapping.HttpStatement;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;

/**
 * 默认网关会话
 * @author yoyocraft
 * @date 2024/10/05
 */
public class DefaultGatewaySession implements GatewaySession {
    private final Configuration configuration;
    private final String uri;
    private final DataSource dataSource;

    public DefaultGatewaySession(Configuration configuration, String uri, DataSource dataSource) {
        this.configuration = configuration;
        this.uri = uri;
        this.dataSource = dataSource;
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public GenericReference getMapper() {
        return configuration.getMapper(uri, this);
    }

    @Override
    public Object get(String methodName, Object args) {
        Connection connection = dataSource.getConnection();
        // FIXME 先写死
        return connection.execute(methodName, new String[]{"java.lang.String"}, new String[]{"name"}, new Object[]{args});
    }
}
