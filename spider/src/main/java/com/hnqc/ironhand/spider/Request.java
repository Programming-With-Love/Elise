package com.hnqc.ironhand.spider;

import com.hnqc.ironhand.spider.model.HttpRequestBody;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Request implements Serializable {
    private static final long serialVersionUID = 2018040215121L;
    public static final String CYCLE_TRIED_TIMES = "_cycle_tried_times";

    private String url;
    private String method;
    private HttpRequestBody requestBody;

    private Map<String, Object> extras;

    private Map<String, String> cookies = new HashMap<String, String>();

    private Map<String, String> headers = new HashMap<String, String>();

    private String charset;

    public Request(Request request) {
        this.url = request.url;
        this.method = request.method;
        this.extras = request.extras;
        this.cookies = request.cookies;
        this.headers = request.headers;
        this.charset = request.charset;
    }

    public Request() {

    }

    public Request(String url) {
        this.url = url;
    }

    public Object getExtra(String key) {
        if (extras == null) {
            return null;
        }
        return extras.get(key);
    }

    public Request putExtra(String key, Object value) {
        if (extras == null) {
            extras = new HashMap<>(16);
        }
        extras.put(key, value);
        return this;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Request request = (Request) o;

        if (url != null ? !url.equals(request.url) : request.url != null) {
            return false;
        }
        return method != null ? method.equals(request.method) : request.method == null;
    }

    public Request addCookie(String name, String value) {
        cookies.put(name, value);
        return this;
    }

    public Request addHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public HttpRequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(HttpRequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, Object> extras) {
        this.extras = extras;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
