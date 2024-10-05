package com.youyi.gateway.bind;

import com.youyi.gateway.session.GatewaySession;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 映射代理调用
 * @author yoyocraft
 * @date 2024/10/05
 */
public class MapperProxy implements MethodInterceptor {
    private final GatewaySession gatewaySession;
    private final String uri;

    public MapperProxy(GatewaySession gatewaySession, String uri) {
        this.gatewaySession = gatewaySession;
        this.uri = uri;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        MapperMethod linkedMethod = new MapperMethod(uri, gatewaySession.getConfiguration());
        return linkedMethod.execute(gatewaySession, args);
    }
}
