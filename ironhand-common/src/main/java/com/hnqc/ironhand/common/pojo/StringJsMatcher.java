package com.hnqc.ironhand.common.pojo;

import com.hnqc.ironhand.common.pojo.entity.Task;
import com.hnqc.ironhand.common.utils.ValidateUtils;

public class StringJsMatcher implements Task.JsMatcher {
    private String jsUrl;

    public StringJsMatcher(String jsUrl) {
        if (ValidateUtils.isEmpty(jsUrl)) {
            throw new IllegalArgumentException("jsUrl can not be null or empty");
        }
        this.jsUrl = jsUrl;
    }

    @Override
    public boolean match(String url) {
        return jsUrl.equals(url);
    }
}
