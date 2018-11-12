package site.zido.elise.processor;

import site.zido.elise.ResultItem;

import java.util.List;

public class ItemLinksModel {
    private List<ResultItem> items;
    private List<String> urls;

    public ItemLinksModel(List<ResultItem> items) {
        this.items = items;
    }

    public ItemLinksModel(List<ResultItem> items, List<String> urls) {
        this.items = items;
        this.urls = urls;
    }

    public List<ResultItem> getItems() {
        return items;
    }

    public void setItems(List<ResultItem> items) {
        this.items = items;
    }

    public List<String> newLinks() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
