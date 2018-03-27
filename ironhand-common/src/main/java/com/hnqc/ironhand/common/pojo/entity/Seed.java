package com.hnqc.ironhand.common.pojo.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.hnqc.ironhand.common.pojo.UrlEntry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class Seed {
    @Id
    private Long id;
    private Long schedulerId;
    private String title;
    @Column(name = "data", length = 10000)
    private String jsonData;
    @Transient
    private List<UrlEntry> urls;
    @Transient
    private List<UrlEntry> scripts;
    @Transient
    private List<UrlEntry> images;


    private Integer parentId;

    public Long getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(Long schedulerId) {
        this.schedulerId = schedulerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }


    public List<UrlEntry> getUrls() {
        return urls;
    }

    public void setUrls(List<UrlEntry> urls) {
        this.urls = urls;
    }

    public List<UrlEntry> getScripts() {
        return scripts;
    }

    public void setScripts(List<UrlEntry> scripts) {
        this.scripts = scripts;
    }

    public List<UrlEntry> getImages() {
        return images;
    }

    public void setImages(List<UrlEntry> images) {
        this.images = images;
    }

    private void add(List<UrlEntry> list, UrlEntry... entry) {
        list.addAll(Arrays.asList(entry));
    }

    public void addToUrls(UrlEntry... entry) {
        if (urls == null) {
            urls = new ArrayList<>();
        }
        add(getUrls(), entry);
    }

    public void addToScripts(UrlEntry... entries) {
        if (scripts == null) {
            scripts = new ArrayList<>();
        }
        add(scripts, entries);
    }

    public void addToImages(UrlEntry... entries) {
        if (images == null) {
            images = new ArrayList<>();
        }
        add(images, entries);
    }

    @Column(length = 2000)
    @JSONField(serialize = false)
    public String getJsonUrl() {
        return JSON.toJSONString(urls);
    }

    public void setJsonUrl(String jsonUrl) {
        this.urls = JSON.parseArray(jsonUrl, UrlEntry.class);
    }

    @Column(length = 2000)
    @JSONField(serialize = false)
    public String getJsonJs() {
        return JSON.toJSONString(scripts);
    }

    public void setJsonJs(String jsonJs) {
        this.scripts = JSON.parseArray(jsonJs, UrlEntry.class);
    }

    @Column(length = 2000)
    @JSONField(serialize = false)
    public String getJsonImg() {
        return JSON.toJSONString(images);
    }

    public void setJsonImg(String jsonImg) {
        this.images = JSON.parseArray(jsonImg, UrlEntry.class);
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
