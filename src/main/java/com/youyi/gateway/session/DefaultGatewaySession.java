package com.youyi.gateway.session;

import com.youyi.gateway.bind.GenericReference;
import com.youyi.gateway.mapping.HttpStatement;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;

/**
 * 默认网关会话
 * @author yoyocraft
 * @date 2024/10/05
 */
public class DefaultGatewaySession implements GatewaySession {
    private final Configuration configuration;

    public DefaultGatewaySession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public GenericReference getMapper(String uri) {
        return configuration.getMapper(uri, this);
    }

    /**
     * TODO 后续拆分到执行器中去执行
     */
    @Override
    public Object get(String uri, Object args) {

        // 获取配置
        HttpStatement httpStatement = configuration.getHttpStatement(uri);
        String applicationName = httpStatement.getApplicationName();
        String interfaceName = httpStatement.getInterfaceName();

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
        // FIXME 先写死
        return genericService.$invoke(httpStatement.getMethodName(), new String[]{"java.lang.String"}, new Object[]{"游艺"});
    }
}
