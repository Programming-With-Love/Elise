package com.hnqc.ironhand.common.pojo;

import com.hnqc.ironhand.common.pojo.entity.Task;
import com.hnqc.ironhand.common.utils.ValidateUtils;

public class StringUrlMatcher implements Task.UrlMatcher {

    private String url;

    public StringUrlMatcher(String url) {
        if (ValidateUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url can not be null or empty");
        }
        this.url = url;
    }

    @Override
    public boolean match(String newUrl, int depth) {
        return url.equals(newUrl);
    }
}
