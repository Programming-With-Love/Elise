package site.zido.elise.proxy;

import site.zido.elise.Page;
import site.zido.elise.Task;

/**
 * Proxy provider.
 *
 * @author zido
 */
public interface ProxyProvider {

    /**
     *
     * Return proxy to Provider when complete a download.
     * @param proxy the proxy config contains host,port and identify info
     * @param page the download result
     * @param task the download task
     */
    void returnProxy(Proxy proxy, Page page, Task task);

    /**
     * Get a proxy for task by some strategy.
     * @param task the download task
     * @return proxy 
     */
    Proxy getProxy(Task task);
    
}
