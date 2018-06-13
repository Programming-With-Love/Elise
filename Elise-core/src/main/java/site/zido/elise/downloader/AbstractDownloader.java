package site.zido.elise.downloader;

import site.zido.elise.Page;
import site.zido.elise.Request;
import site.zido.elise.Site;
import site.zido.elise.selector.Html;

/**
 * abstract downloader,which support charset
 *
 * @author zido
 */
public abstract class AbstractDownloader implements Downloader {
    public Html download(String url) {
        return download(url, null);
    }

    public Html download(String url, String charset) {
        Page page = download(new Request(url), new Site().setCharset(charset).toTask());
        return page.html();
    }

    public Page downloadAsPage(String url) {
        return download(new Request(url), new Site().toTask());
    }

    protected void onSuccess(Request request) {
    }

    protected void onError(Request request) {
    }
}
