package com.hnqc.ironhand.spider.downloader;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Site;
import com.hnqc.ironhand.spider.selector.Html;

public abstract class AbsDownloader implements Downloader {
    public Html download(String url) {
        return download(url, null);
    }

    public Html download(String url, String charset) {
        Page page = download(new Request(url), new Site().setCharset(charset).toTask());
        return (Html) page.getHtml();
    }

    protected void onSuccess(Request request) {
    }

    protected void onError(Request request) {
    }
}
