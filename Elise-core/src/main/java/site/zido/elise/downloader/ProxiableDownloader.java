package site.zido.elise.downloader;

import site.zido.elise.proxy.ProxyProvider;

/**
 * The interface Proxiable downloader.
 *
 * @author zido
 */
public interface ProxiableDownloader extends Downloader {
    /**
     * Sets proxy provider.
     *
     * @param provider the provider
     */
    void setProxyProvider(ProxyProvider provider);
}
