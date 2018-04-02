package com.hnqc.ironhand.spider.proxy;

import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.Task;

/**
 * Proxy provider. <br>
 *     
 * @since 0.7.0
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
