package site.zido.elise.downloader;

import site.zido.elise.proxy.ProxyProvider;

public interface ProxiableDownloader extends Downloader {
    void setProxyProvider(ProxyProvider provider);
}
