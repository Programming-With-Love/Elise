package com.hnqc.ironhand.common.pojo;

public class UrlEntry {
    /**
     * 真实路径
     */
    private String name;
    /**
     * 处理后的路径
     */
    private String value;

    /**
     * 尝试下载次数
     */
    private Integer tryTimes = 0;

    public UrlEntry() {

    }

    public UrlEntry(String name) {
        this.name = name;
    }

    public UrlEntry(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getTryTimes() {
        return tryTimes;
    }

    public void setTryTimes(Integer tryTimes) {
        this.tryTimes = tryTimes;
    }
}
