package com.server.model;

import java.util.Map;

public class HttpEntity {
    private String name;
    //request
    private String host;
    private String method;
    private String path;
    private boolean isSuccess;
    //response
    private int statusCode;
    private Map responseHeaders;
    private Object responseBody;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Map getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Map responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public Object getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(Object responseBody) {
        this.responseBody = responseBody;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    @Override
    public String toString() {
        return "HttpEntity{" +
                "name='" + name + '\'' +
                ", host='" + host + '\'' +
                ", method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", isSuccess=" + isSuccess +
                ", statusCode=" + statusCode +
                ", responseHeaders=" + responseHeaders +
                ", responseBody='" + responseBody + '\'' +
                '}';
    }
}
