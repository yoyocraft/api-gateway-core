package com.youyi.gateway.bind;

import com.google.common.collect.Maps;
import com.youyi.gateway.session.Configuration;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.Map;
import java.util.Objects;

/**
 * 泛化调用注册器
 * @author yoyocraft
 * @date 2024/10/04
 */
public class GenericReferenceRegistry {

    private static final Map<String /* methodName */, GenericReferenceProxyFactory> KNOWN_GENERIC_REFS = Maps.newConcurrentMap();

    private final Configuration configuration;

    public GenericReferenceRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    public GenericReference getGenericReference(String methodName) {
        GenericReferenceProxyFactory proxyFactory = KNOWN_GENERIC_REFS.get(methodName);
        if (Objects.isNull(proxyFactory)) {
            throw new RuntimeException("Type " + methodName + " is not known to the GenericReferenceRegistry");
        }
        return proxyFactory.newInstance(methodName);
    }

    public void addGenericReference(String applicationName, String interfaceName, String methodName) {
        // 获取基础服务（创建成本高，内存获取）
        ApplicationConfig applicationConfig = configuration.getApplicationConfig(applicationName);
        RegistryConfig registryConfig = configuration.getRegistryConfig(applicationName);
        ReferenceConfig<GenericService> referenceConfig = configuration.getReferenceConfig(interfaceName);

        // 构建 Dubbo 服务
        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        bootstrap
                .application(applicationConfig)
                .registry(registryConfig)
                .reference(referenceConfig)
                .start();

        // 获取泛化调用服务
        GenericService genericService = ReferenceConfigCache.getCache().get(referenceConfig);

        // 创建并保存泛化服务工厂
        KNOWN_GENERIC_REFS.put(methodName, new GenericReferenceProxyFactory(genericService));
    }
}
