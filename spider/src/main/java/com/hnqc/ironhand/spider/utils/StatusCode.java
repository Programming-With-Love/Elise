package com.hnqc.ironhand.spider.utils;

/**
 * http状态码
 *
 * @author zido
 * @date 2018/45/12
 */
public class StatusCode {
    private StatusCode() {
    }

    public static final int CODE_200 = 200;
    public static final int CODE_300 = 300;

    public static boolean isSuccess(int code) {
        return code >= CODE_200 && code <= CODE_300;
    }
}
