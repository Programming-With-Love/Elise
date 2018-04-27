package com.hnqc.ironhand;

import com.hnqc.ironhand.common.SavedPage;
import com.hnqc.ironhand.downloader.HttpClientDownloader;
import com.hnqc.ironhand.selector.Html;
import com.hnqc.ironhand.selector.PlainText;

/**
 * OssReadListener
 *
 * @author zido
 * @date 2018/04/27
 */
public class OssReadListener implements SavedPage.ReadListener {
    private HttpClientDownloader clientDownloader = new HttpClientDownloader();

    @Override
    public String read(String url, PlainText originUrl) {
        Page download = clientDownloader.downloadAsPage(url);
        Page page = download.setUrl(originUrl);
        return page.html().get();
    }
}
