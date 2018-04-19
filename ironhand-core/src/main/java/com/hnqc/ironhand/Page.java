package com.hnqc.ironhand;

import com.hnqc.ironhand.selector.Html;
import com.hnqc.ironhand.selector.PlainText;
import com.hnqc.ironhand.utils.StatusCode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 下载的页面对象
 *
 * @author zido
 * @date 2018/33/12
 */
public class Page {
    private static final String HASH_SEP = "#";

    private Html html;

    private String rawText;

    private PlainText url;

    private Map<String, List<String>> headers;

    private int statusCode = StatusCode.CODE_200;

    private boolean downloadSuccess = true;

    private byte[] bytes;

    private String charset;

    public Page() {
    }

    public static Page fail() {
        Page page = new Page();
        page.setDownloadSuccess(false);
        return page;
    }

    /**
     * get html content of page
     *
     * @return html
     */
    public Html getHtml() {
        if (html == null) {
            html = new Html(rawText, url.toString());
        }
        return html;
    }

    /**
     * @param html html
     * @deprecated since 0.4.0
     * The html is parse just when first time of calling {@link #getHtml()}, so use {@link #setRawText(String)} instead.
     */
    public void setHtml(Html html) {
        this.html = html;
    }

    /**
     * get url of current page
     *
     * @return url of current page
     */
    public PlainText getUrl() {
        return url;
    }

    public void setUrl(PlainText url) {
        this.url = url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getRawText() {
        return rawText;
    }

    public Page setRawText(String rawText) {
        this.rawText = rawText;
        return this;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public boolean isDownloadSuccess() {
        return downloadSuccess;
    }

    public void setDownloadSuccess(boolean downloadSuccess) {
        this.downloadSuccess = downloadSuccess;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    @Override
    public String toString() {
        return "Page{" +
                ", html=" + html +
                ", rawText='" + rawText + '\'' +
                ", url=" + url +
                ", headers=" + headers +
                ", statusCode=" + statusCode +
                ", downloadSuccess=" + downloadSuccess +
                ", charset='" + charset + '\'' +
                ", bytes=" + Arrays.toString(bytes) +
                '}';
    }
}
