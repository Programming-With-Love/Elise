package com.hnqc.ironhand.schedule.pojo;

public class AjaxResult<T> {
    private T Data;
    private int code;

    public static <T> AjaxResult<T> success() {
        return new AjaxResult<T>().setCode(0);
    }

    public T getData() {
        return Data;
    }

    public AjaxResult<T> setData(T data) {
        Data = data;
        return this;
    }

    public int getCode() {
        return code;
    }

    public AjaxResult<T> setCode(int code) {
        this.code = code;
        return this;
    }
}
