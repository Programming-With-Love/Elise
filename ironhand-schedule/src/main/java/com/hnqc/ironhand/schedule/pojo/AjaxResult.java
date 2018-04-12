package com.hnqc.ironhand.schedule.pojo;

public class AjaxResult<T> {
    private T data;
    private int code;

    public static <T> AjaxResult<T> success() {
        return new AjaxResult<T>().setCode(0);
    }

    public T getData() {
        return data;
    }

    public AjaxResult<T> setData(T data) {
        this.data = data;
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
