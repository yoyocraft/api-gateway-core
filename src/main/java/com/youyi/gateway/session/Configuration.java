package com.youyi.gateway.session;

import com.google.common.collect.Maps;
import com.youyi.gateway.bind.GenericReference;
import com.youyi.gateway.bind.GenericReferenceRegistry;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.Map;

/**
 * Session 生命周期配置项
 * @author yoyocraft
 * @date 2024/10/05
 */
public class Configuration {

    private static final Map<String /* appName */, ApplicationConfig> APPLICATION_CONFIG_CACHE = Maps.newHashMap();
    private static final Map<String /* appName */, RegistryConfig> REGISTRY_CONFIG_CACHE = Maps.newHashMap();
    private static final Map<String /* interfaceName */, ReferenceConfig<GenericService>> REFERENCE_CONFIG_CACHE = Maps.newHashMap();

    private final GenericReferenceRegistry referenceRegistry;

    public Configuration() {
        referenceRegistry = new GenericReferenceRegistry(this);

        // TODO youyi 2024/10/5 从配置文件中获取
        final String applicationName = "api-gateway-test";
        final String interfaceName = "com.youyi.gateway.api.TestService";

        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(applicationName);
        applicationConfig.setQosEnable(Boolean.FALSE);
        APPLICATION_CONFIG_CACHE.put(applicationName, applicationConfig);

        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("zookeeper://127.0.0.1:2181");
        registryConfig.setRegister(Boolean.FALSE);
        REGISTRY_CONFIG_CACHE.put(applicationName, registryConfig);

        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(interfaceName);
        referenceConfig.setVersion("1.0.0");
        referenceConfig.setGeneric("true");
        REFERENCE_CONFIG_CACHE.put(interfaceName, referenceConfig);
    }

    public ApplicationConfig getApplicationConfig(String applicationName) {
        return APPLICATION_CONFIG_CACHE.get(applicationName);
    }

    public RegistryConfig getRegistryConfig(String applicationName) {
        return REGISTRY_CONFIG_CACHE.get(applicationName);
    }

    public ReferenceConfig<GenericService> getReferenceConfig(String interfaceName) {
        return REFERENCE_CONFIG_CACHE.get(interfaceName);
    }

    public void addGenericReference(String applicationName, String interfaceName, String methodName) {
        referenceRegistry.addGenericReference(applicationName, interfaceName, methodName);
    }

    public GenericReference getGenericReference(String methodName) {
        return referenceRegistry.getGenericReference(methodName);
    }
}
