package site.zido.elise.processor;

import java.util.List;

/**
 * The type Item links model.
 *
 * @author zido
 */
public class ItemLinksModel {
    private List<ResultItem> items;
    private List<String> urls;

    /**
     * Instantiates a new Item links model.
     *
     * @param items the items
     */
    public ItemLinksModel(List<ResultItem> items) {
        this.items = items;
    }

    /**
     * Instantiates a new Item links model.
     *
     * @param items the items
     * @param urls  the urls
     */
    public ItemLinksModel(List<ResultItem> items, List<String> urls) {
        this.items = items;
        this.urls = urls;
    }

    /**
     * Gets items.
     *
     * @return the items
     */
    public List<ResultItem> getItems() {
        return items;
    }

    /**
     * Sets items.
     *
     * @param items the items
     */
    public void setItems(List<ResultItem> items) {
        this.items = items;
    }

    /**
     * New links list.
     *
     * @return the list
     */
    public List<String> newLinks() {
        return urls;
    }

    /**
     * Sets urls.
     *
     * @param urls the urls
     */
    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
