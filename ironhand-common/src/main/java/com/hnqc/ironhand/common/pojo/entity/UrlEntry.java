package com.hnqc.ironhand.common.pojo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class UrlEntry {
    @Id
    @GeneratedValue
    private String id;
    /**
     * 真实路径
     */
    private String name;
    /**
     * 处理后的路径
     */
    private String value;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
