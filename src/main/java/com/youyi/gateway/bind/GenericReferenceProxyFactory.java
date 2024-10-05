package com.youyi.gateway.bind;

import com.google.common.collect.Maps;
import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InterfaceMaker;
import org.apache.dubbo.rpc.service.GenericService;
import org.objectweb.asm.Type;

import java.util.Map;

/**
 * 泛化调用服务代理工厂
 * @author yoyocraft
 * @date 2024/10/04
 */
public class GenericReferenceProxyFactory {

    /**
     * 缓存泛化调用服务代理
     */
    private static final Map<String /* methodName */, GenericReference> GENERIC_REF_CACHE = Maps.newConcurrentMap();

    private final GenericService genericService;

    public GenericReferenceProxyFactory(GenericService genericService) {
        this.genericService = genericService;
    }

    public GenericReference newInstance(String method) {
        return GENERIC_REF_CACHE.computeIfAbsent(method, (k) -> {
            GenericReferenceProxy genericReferenceProxy = new GenericReferenceProxy(genericService, method);

            InterfaceMaker interfaceMaker = new InterfaceMaker();
            interfaceMaker.add(
                    new Signature(method, Type.getType(String.class), new Type[]{Type.getType(String.class)}),
                    new Type[0]
            );
            Class<?> interfaceClass = interfaceMaker.create();

            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(Object.class);
            // 代理类实现的两个接口：
            // 1) interfaceClass: 根据泛化调用注册信息创建的接口，建立 HTTP-RPC 的映射
            // 2) GenericReference: 用于返回泛化调用结果
            enhancer.setInterfaces(new Class[]{interfaceClass, GenericReference.class});
            enhancer.setCallback(genericReferenceProxy);
            return (GenericReference) enhancer.create();
        });
    }
}
