package com.youyi.gateway.datasource.connection;

import com.youyi.gateway.datasource.Connection;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import java.io.IOException;

/**
 * Http Connection
 * @author yoyocraft
 * @date 2024/10/06
 */
public class HttpConnection implements Connection {
    private final HttpClient httpClient;
    private final HttpPost httpPost;

    public HttpConnection(String uri) {
        this.httpClient = HttpClients.createDefault();
        httpPost = new HttpPost(uri);
        httpPost.setHeader("accept", "*/*");
        httpPost.setHeader("connection", "Keep-Alive");
        httpPost.setHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
    }

    @Override
    public Object execute(String method, String[] parameterTypes, String[] parameterNames, Object[] args) {
        String ret;
        try {
            ret = httpClient.execute(httpPost, new BasicHttpClientResponseHandler());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }
}
