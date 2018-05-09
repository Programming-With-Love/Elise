package site.zido.elise;

import site.zido.elise.common.SavedPage;
import site.zido.elise.downloader.HttpClientDownloader;
import site.zido.elise.selector.Html;
import site.zido.elise.selector.PlainText;

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
