package com.youyi.gateway.bind;

import com.google.common.collect.Maps;
import com.youyi.gateway.mapping.HttpStatement;
import com.youyi.gateway.session.Configuration;
import com.youyi.gateway.session.GatewaySession;

import java.util.Map;
import java.util.Objects;

/**
 * 泛化调用注册器
 * @author yoyocraft
 * @date 2024/10/05
 */
public class MapperRegistry {
    private static final Map<String /* uri */, MapperProxyFactory> KNOWN_MAPPERS = Maps.newConcurrentMap();

    private final Configuration configuration;

    public MapperRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    public GenericReference getMapper(String uri, GatewaySession session) {
        final MapperProxyFactory mapperProxyFactory = KNOWN_MAPPERS.get(uri);
        if (Objects.isNull(mapperProxyFactory)) {
            throw new RuntimeException("Uri " + uri + " is not known to the MapperRegistry");
        }

        return mapperProxyFactory.newInstance(session);
    }

    public void addMapper(HttpStatement httpStatement) {
        String uri = httpStatement.getUri();
        if (hasMapper(uri)) {
            throw new RuntimeException("Uri " + uri + " is already known to the MapperRegistry.");
        }
        KNOWN_MAPPERS.put(uri, new MapperProxyFactory(uri));
        // 保存接口映射关系
        configuration.addHttpStatement(httpStatement);
    }

    private boolean hasMapper(String uri) {
        return KNOWN_MAPPERS.containsKey(uri);
    }
}
