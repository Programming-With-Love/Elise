package com.hnqc.ironhand;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 结果集
 *
 * @author zido
 * @date 2018/29/12
 */
public class ResultItem {
    private Map<String, Object> fields = new LinkedHashMap<>();

    private Request request;

    private boolean skip = false;

    public <T> T get(String key) {
        Object o = fields.get(key);
        if (o == null) {
            return null;
        }
        return (T) fields.get(key);
    }

    public Map<String, Object> getAll() {
        return fields;
    }

    public <T> ResultItem put(String key, T value) {
        fields.put(key, value);
        return this;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public boolean isSkip() {
        return skip;
    }

    public ResultItem setSkip(boolean skip) {
        this.skip = skip;
        return this;
    }
}
