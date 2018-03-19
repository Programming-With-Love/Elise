package com.hnqc.ironhand.schedule.pojo;

public enum RespCode {
    SUCCESS(0),
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
