package com.youyi.gateway.basic;

import com.alibaba.fastjson2.JSON;
import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InterfaceMaker;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.objectweb.asm.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author yoyocraft
 * @date 2024/10/05
 */
@RunWith(JUnit4.class)
public class CglibTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CglibTest.class);
    final String methodName = "sayHi";

    Object proxy;

    @Before
    public void setup() {
        InterfaceMaker interfaceMaker = new InterfaceMaker();
        interfaceMaker.add(
                new Signature(methodName, Type.getType(String.class), new Type[]{Type.getType(String.class)}),
                new Type[0]
        );
        Class<?> interfaceClass = interfaceMaker.create();

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Object.class);
        enhancer.setInterfaces(new Class[]{interfaceClass});
        enhancer.setCallback(new TestInterceptor());

        proxy = enhancer.create();
    }

    @Test
    public void test_cglib() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = proxy.getClass().getMethod(methodName, String.class);
        Object ret = method.invoke(proxy, "yoyocraft");
        LOGGER.info("[CglibTest] ret: {}", ret);
    }

    static class TestInterceptor implements MethodInterceptor {

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            return JSON.toJSONString(args) + " " + System.currentTimeMillis();
        }
    }
}
