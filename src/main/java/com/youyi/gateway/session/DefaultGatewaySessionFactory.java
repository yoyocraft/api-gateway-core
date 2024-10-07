package com.youyi.gateway.session;

import com.youyi.gateway.datasource.DataSource;
import com.youyi.gateway.datasource.DataSourceFactory;
import com.youyi.gateway.datasource.UnpooledDataSourceFactory;

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
    public GatewaySession openSession(String uri) throws Exception {
        DataSourceFactory dataSourceFactory = new UnpooledDataSourceFactory();
        dataSourceFactory.setProperties(configuration, uri);
        DataSource dataSource = dataSourceFactory.getDataSource();
        return new DefaultGatewaySession(configuration, uri, dataSource);
    }
}
