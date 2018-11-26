package site.zido.elise.downloader;

import site.zido.elise.http.impl.DefaultResponse;
import site.zido.elise.http.impl.DefaultRequest;
import site.zido.elise.Site;
import site.zido.elise.Task;
import site.zido.elise.proxy.ProxyProvider;

/**
 * Auto switch downloader.
 *
 * @author zido
 */
public class AutoSwitchDownloader implements Downloader {
    /**
     * The constant DOWNLOAD_MODE.
     */
    public static final String DOWNLOAD_MODE = "downloadMode";
    /**
     * The constant DOWNLOAD_MODE_NORMAL.
     */
    public static final String DOWNLOAD_MODE_NORMAL = "normal";
    /**
     * The constant DOWNLOAD_MODE_HTML_UNIT.
     */
    public static final String DOWNLOAD_MODE_HTML_UNIT = "htmlUnit";
    private HttpClientDownloader httpClientDownloader;
    private HtmlUnitDownloader htmlUnitDownloader;

    /**
     * Instantiates a new Auto switch downloader.
     */
    public AutoSwitchDownloader() {
        this.httpClientDownloader = new HttpClientDownloader();
        this.htmlUnitDownloader = new HtmlUnitDownloader();
    }

    @Override
    public DefaultResponse download(Task task, DefaultRequest request) {
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

    /**
     * Sets proxy provider.
     *
     * @param proxyProvider the proxy provider
     * @return the proxy provider
     */
    public AutoSwitchDownloader setProxyProvider(ProxyProvider proxyProvider) {
        this.httpClientDownloader.setProxyProvider(proxyProvider);
        this.htmlUnitDownloader.setProxyProvider(proxyProvider);
        return this;
    }
}
