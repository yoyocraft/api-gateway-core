package com.youyi.gateway.bind;

import com.youyi.gateway.mapping.HttpCmdType;
import com.youyi.gateway.session.Configuration;
import com.youyi.gateway.session.GatewaySession;

/**
 * 处理不同的请求，调用 Session
 * @author yoyocraft
 * @date 2024/10/05
 */
public class MapperMethod {
    private final String methodName;
    private final HttpCmdType httpCmdType;

    public MapperMethod(String uri, Configuration configuration) {
        this.methodName = configuration.getHttpStatement(uri).getMethodName();
        this.httpCmdType = configuration.getHttpStatement(uri).getHttpCmdType();
    }

    public Object execute(GatewaySession session, Object args) {
        Object ret = null;
        switch (httpCmdType) {
            case GET:
                ret = session.get(methodName, args);
                break;
            // TODO youyi 2024/10/5 后续支持更多的 HTTP 方式
            case POST:
            case PUT:
            case DELETE:
            case OPTIONS:
                break;
            default:
                throw new RuntimeException("unsupported http cmd type:" + httpCmdType);
        }
        return ret;
    }
}
