package site.zido.elise.downloader;

import site.zido.elise.Task;
import site.zido.elise.custom.Config;
import site.zido.elise.custom.GlobalConfig;
import site.zido.elise.http.Request;
import site.zido.elise.http.Response;
import site.zido.elise.proxy.ProxyProvider;

/**
 * Auto switch downloader.
 *
 * @author zido
 */
public class AutoSwitchDownloader implements Downloader {
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
    public Response download(Task task, Request request) {
        final Config config = task.modelExtractor().getConfig();
        String mode = config.get(GlobalConfig.KEY_DOWNLOAD_MODE);
        if (DOWNLOAD_MODE_HTML_UNIT.equalsIgnoreCase(String.valueOf(mode))) {
            return htmlUnitDownloader.download(task, request);
        } else if (DOWNLOAD_MODE_NORMAL.equalsIgnoreCase(String.valueOf(mode))) {
            return httpClientDownloader.download(task, request);
        }
        return httpClientDownloader.download(task, request);
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
