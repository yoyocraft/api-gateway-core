package com.youyi.gateway.mapping;

/**
 * 网关接口映射信息
 * @author yoyocraft
 * @date 2024/10/05
 */
public class HttpStatement {

    // rpc 相关
    private final String applicationName;
    private final String interfaceName;
    private final String methodName;

    // http 相关
    private final String uri;
    private final HttpCmdType httpCmdType;

    public HttpStatement(String applicationName, String interfaceName, String methodName, String uri, HttpCmdType httpCmdType) {
        this.applicationName = applicationName;
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.uri = uri;
        this.httpCmdType = httpCmdType;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getUri() {
        return uri;
    }

    public HttpCmdType getHttpCmdType() {
        return httpCmdType;
    }
}
