package com.hnqc.ironhand;

import com.hnqc.ironhand.common.SavedPage;
import com.hnqc.ironhand.downloader.HttpClientDownloader;
import com.hnqc.ironhand.selector.Html;

/**
 * OssReadLisener
 *
 * @author zido
 * @date 2018/04/27
 */
public class OssReadLisener implements SavedPage.ReadListener {
    private HttpClientDownloader clientDownloader = new HttpClientDownloader();

    @Override
    public String read(String url) {
        Html download = clientDownloader.download(url);
        return download.get();
    }
}
