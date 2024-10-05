package com.youyi.gateway.basic;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yoyocraft
 * @date 2024/10/05
 */
@RunWith(JUnit4.class)
public class RpcTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcTest.class);

    GenericService genericService;

    @Before
    public void setup() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("api-gateway-test");
        applicationConfig.setQosEnable(Boolean.FALSE);

        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("zookeeper://127.0.0.1:2181");
        registryConfig.setRegister(Boolean.FALSE);

        ReferenceConfig<GenericService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface("com.youyi.gateway.api.TestService");
        referenceConfig.setVersion("1.0.0");
        referenceConfig.setGeneric("true");

        DubboBootstrap bootstrap = DubboBootstrap.getInstance();
        bootstrap
                .application(applicationConfig)
                .registry(registryConfig)
                .reference(referenceConfig)
                .start();

        genericService = ReferenceConfigCache.getCache().get(referenceConfig);
    }

    @Test
    public void test_dubbo() {
        Object ret = genericService.$invoke("sayHi", new String[]{"java.lang.String"}, new Object[]{"yoyocraft"});
        LOGGER.info("ret: {}", ret);
    }
}
