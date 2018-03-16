package com.hnqc.ironhand.common.pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SeedData {
    private List<UrlEntry> html;
    private List<UrlEntry> js;
    private List<UrlEntry> img;

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

    private void add(List<UrlEntry> list, UrlEntry... entry) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.addAll(Arrays.asList(entry));
    }

    public void addToHtml(UrlEntry... entry) {
        add(html, entry);
    }

    public void addToJs(UrlEntry... entries) {
        add(js, entries);
    }

    public void addToImg(UrlEntry... entries) {
        add(img, entries);
    }
}
