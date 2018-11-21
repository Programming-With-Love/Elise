package site.zido.elise.downloader;

import site.zido.elise.Page;
import site.zido.elise.Request;
import site.zido.elise.Site;
import site.zido.elise.Task;
import site.zido.elise.proxy.ProxyProvider;

/**
 * AutoSwitchDownloader
 *
 * @author zido
 */
public class AutoSwitchDownloader implements Downloader {
    public static final String DOWNLOAD_MODE = "downloadMode";
    public static final String DOWNLOAD_MODE_NORMAL = "normal";
    public static final String DOWNLOAD_MODE_HTML_UNIT = "htmlUnit";
    private HttpClientDownloader httpClientDownloader;
    private HtmlUnitDownloader htmlUnitDownloader;

    public AutoSwitchDownloader() {
        this.httpClientDownloader = new HttpClientDownloader();
        this.htmlUnitDownloader = new HtmlUnitDownloader();
    }

    @Override
    public Page download(Task task, Request request) {
        Site site = task.getSite();
        Object extra = site.getExtra(DOWNLOAD_MODE);
        if (DOWNLOAD_MODE_HTML_UNIT.equalsIgnoreCase(String.valueOf(extra))) {
            return htmlUnitDownloader.download(task, request);
        } else {
            return httpClientDownloader.download(task, request);
        }
    }

    @Override
    public void setThread(int threadNum) {
        httpClientDownloader.setThread(threadNum);
    }

    public AutoSwitchDownloader setProxyProvider(ProxyProvider proxyProvider) {
        this.httpClientDownloader.setProxyProvider(proxyProvider);
        this.htmlUnitDownloader.setProxyProvider(proxyProvider);
        return this;
    }
}
