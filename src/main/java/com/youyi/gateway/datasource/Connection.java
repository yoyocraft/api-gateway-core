package com.youyi.gateway.datasource;

/**
 * 连接接口
 * @author yoyocraft
 * @date 2024/10/06
 */
public interface Connection {

    Object execute(String method, String[] parameterTypes, String[] parameterNames, Object[] args);
}
