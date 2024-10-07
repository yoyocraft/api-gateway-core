package com.youyi.gateway.datasource.connection;

import com.youyi.gateway.datasource.Connection;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;

/**
 * RPC Dubbo Connection
 * @author yoyocraft
 * @date 2024/10/06
 */
public class DubboConnection implements Connection {
    private final GenericService genericService;

    public DubboConnection(
            ApplicationConfig applicationConfig,
            RegistryConfig registryConfig,
            ReferenceConfig<GenericService> referenceConfig
    ) {
        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        bootstrap
                .application(applicationConfig)
                .registry(registryConfig)
                .reference(referenceConfig)
                .start();

        genericService = ReferenceConfigCache.getCache().get(referenceConfig);
    }

    @Override
    public Object execute(String method, String[] parameterTypes, String[] parameterNames, Object[] args) {
        return genericService.$invoke(method, parameterTypes, args);
    }
}
