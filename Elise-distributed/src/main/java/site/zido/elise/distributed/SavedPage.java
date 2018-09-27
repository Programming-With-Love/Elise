package site.zido.elise.distributed;

import site.zido.elise.Page;
import site.zido.elise.selector.PlainText;

/**
 * SavedPage
 *
 * @author zido
 */
public class SavedPage {
    private String url;
    private Page page;

    /**
     * The interface Saved listener.
     */
    public interface SavedListener {

        /**
         * On save string.
         *
         * @param rawText the raw text
         * @return the url
         */
        String onSave(byte[] rawText);
    }

    /**
     * The interface Read listener.
     */
    public interface ReadListener {
        /**
         * Read string.
         *
         * @param url the url
         * @param originUrl origin url
         * @return the string
         */
        String read(String url, String originUrl);
    }

    /**
     * Resolve page saved page.
     *
     * @param page     the page
     * @param listener the listener
     * @return the saved page
     */
    public static SavedPage resolvePage(Page page, SavedListener listener) {
        byte[] bytes = page.getBytes();
        if (bytes == null) {
            String rawText = page.getRawText();
            if(rawText == null){
                return null;
            }
            bytes = rawText.getBytes();
        }
        page.setRawText(null);
        page.setBytes(null);
        SavedPage savedPage = new SavedPage();
        savedPage.url = listener.onSave(bytes);
        savedPage.page = page;
        return savedPage;
    }

    /**
     * Gets page.
     *
     * @param savedPage the saved page
     * @param listener  the listener
     * @return the page
     */
    public static Page resolvePage(SavedPage savedPage, ReadListener listener) {
        Page page = savedPage.page;
        String rawText = listener.read(savedPage.url, page.getUrl());
        page.setRawText(rawText);
        return page;
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets url.
     *
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets page.
     *
     * @return the page
     */
    public Page getPage() {
        return page;
    }

    /**
     * Sets page.
     *
     * @param page the page
     */
    public void setPage(Page page) {
        this.page = page;
    }
}
