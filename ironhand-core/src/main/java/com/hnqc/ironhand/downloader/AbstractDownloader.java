package com.hnqc.ironhand.downloader;

import com.hnqc.ironhand.Page;
import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Site;
import com.hnqc.ironhand.selector.Html;

/**
 * abstract downloader,which support charset
 *
 * @author zido
 * @date 2018/41/12
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
