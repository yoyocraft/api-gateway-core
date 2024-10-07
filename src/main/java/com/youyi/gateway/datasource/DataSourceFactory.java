package com.youyi.gateway.datasource;

import com.youyi.gateway.session.Configuration;

/**
 * 数据源工厂
 * @author yoyocraft
 * @date 2024/10/06
 */
public interface DataSourceFactory {

    void setProperties(Configuration configuration, String uri);

    DataSource getDataSource();
}
