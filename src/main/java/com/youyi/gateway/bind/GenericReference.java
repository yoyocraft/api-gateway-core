package com.youyi.gateway.bind;

/**
 * 泛化调用统一接口
 * @author yoyocraft
 * @date 2024/10/04
 */
public interface GenericReference {

    /**
     * FIXME 目前只在这里提供一个标准的 String 的入参和出参类型的泛化调用接口。
     * @param args 参数
     * @return 结果
     */
    String $invoke(String args);
}
