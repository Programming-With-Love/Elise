package com.hnqc.ironhand.schedule.pojo;

/**
 * 响应码
 *
 * @author zido
 * @date 2018/36/12
 */
public enum RespCode {
    /**
     * 请求成功
     */
    SUCCESS(0),
    /**
     * 请求失败
     */
    WARN(-1);

    private int code;

    RespCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
