package site.zido.elise.downloader;

import site.zido.elise.Page;
import site.zido.elise.Request;
import site.zido.elise.Site;

/**
 * abstract downloader,which support charset
 *
 * @author zido
 */
public abstract class AbstractDownloader implements Downloader {

    public Page downloadAsPage(String url) {
        return download(new Site().toTask(), new Request(url));
    }

    protected void onSuccess(Request request) {
    }

    protected void onError(Request request) {
    }
}
