package com.youyi.gateway.datasource;

/**
 * 数据源接口，RPC、HTTP 都当作连接数据源来使用
 * @author yoyocraft
 * @date 2024/10/06
 */
public interface DataSource {

    Connection getConnection();
}
