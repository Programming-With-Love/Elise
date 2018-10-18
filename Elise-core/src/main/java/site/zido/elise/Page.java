package site.zido.elise;

import site.zido.elise.select.Selectable;
import site.zido.elise.select.Text;
import site.zido.elise.utils.StatusCode;

import java.util.List;
import java.util.Map;

/**
 * 下载的页面对象
 *
 * @author zido
 */
public class Page {
    private Text url;
    private Map<String, List<String>> headers;
    private int statusCode = StatusCode.CODE_200;

    private Selectable body;

    private boolean downloadSuccess = true;

    private String charset;

    public Page() {
    }

    public static Page fail() {
        Page page = new Page();
        page.setDownloadSuccess(false);
        page.setStatusCode(-1);
        return page;
    }

    /**
     * get url of current page
     *
     * @return url of current page
     */
    public Text getUrl() {
        return url;
    }

    public Page setUrl(Text url) {
        this.url = url;
        return this;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getRawText() {
        return null;
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

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    @Override
    public String toString() {
        return "Page{" +
                "url='" + url + '\'' +
                ", headers=" + headers +
                ", statusCode=" + statusCode +
                ", body=" + body +
                ", downloadSuccess=" + downloadSuccess +
                ", charset='" + charset + '\'' +
                '}';
    }

    public Selectable getBody() {
        return body;
    }

    public void setBody(Selectable body) {
        this.body = body;
    }
}
