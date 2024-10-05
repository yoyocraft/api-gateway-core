package com.youyi.gateway.bind;

import com.google.common.collect.Maps;
import com.youyi.gateway.mapping.HttpStatement;
import com.youyi.gateway.session.GatewaySession;
import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InterfaceMaker;
import org.objectweb.asm.Type;

import java.util.Map;

/**
 * MapperProxy 代理工厂
 * @author yoyocraft
 * @date 2024/10/05
 */
public class MapperProxyFactory {

    /**
     * 缓存泛化调用服务代理
     */
    private static final Map<String /* uri */, GenericReference> GENERIC_REF_CACHE = Maps.newConcurrentMap();

    private final String uri;

    public MapperProxyFactory(String uri) {
        this.uri = uri;
    }

    public GenericReference newInstance(GatewaySession session) {
        return GENERIC_REF_CACHE.computeIfAbsent(uri, (k) -> {
            HttpStatement httpStatement = session.getConfiguration().getHttpStatement(uri);
            // 泛化调用
            MapperProxy mapperProxy = new MapperProxy(session, uri);

            InterfaceMaker interfaceMaker = new InterfaceMaker();
            interfaceMaker.add(
                    new Signature(
                            httpStatement.getMethodName(),
                            Type.getType(String.class),
                            new Type[]{Type.getType(String.class)}
                    ),
                    new Type[0]
            );
            Class<?> interfaceClass = interfaceMaker.create();

            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(Object.class);
            // 代理类实现的两个接口：
            // 1) interfaceClass: 根据泛化调用注册信息创建的接口，建立 HTTP-RPC 的映射
            // 2) GenericReference: 用于返回泛化调用结果
            enhancer.setInterfaces(new Class[]{interfaceClass, GenericReference.class});
            enhancer.setCallback(mapperProxy);
            return (GenericReference) enhancer.create();
        });
    }
}
