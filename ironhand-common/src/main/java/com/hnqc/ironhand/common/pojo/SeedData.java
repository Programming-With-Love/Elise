package com.hnqc.ironhand.common.pojo;

import com.hnqc.ironhand.common.pojo.entity.UrlEntry;

import java.util.ArrayList;
import java.util.List;

public class SeedData {
    private Integer id;
    private List<UrlEntry> html = new ArrayList<>();
    private List<UrlEntry> js = new ArrayList<>();
    private List<UrlEntry> img = new ArrayList<>();

    public List<UrlEntry> getHtml() {
        return html;
    }

    public void setHtml(List<UrlEntry> html) {
        this.html = html;
    }

    public List<UrlEntry> getJs() {
        return js;
    }

    public void setJs(List<UrlEntry> js) {
        this.js = js;
    }

    public List<UrlEntry> getImg() {
        return img;
    }

    public void setImg(List<UrlEntry> img) {
        this.img = img;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
