package com.youyi.gateway.session;

import com.youyi.gateway.bind.GenericReference;

/**
 * 网关会话服务
 * @author yoyocraft
 * @date 2024/10/05
 */
public interface GatewaySession {

    Configuration getConfiguration();

    GenericReference getMapper(String uri);

    /**
     * {@link com.youyi.gateway.mapping.HttpCmdType#GET}
     */
    Object get(String uri, Object args);
}
