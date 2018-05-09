package site.zido.elise.downloader;

import site.zido.elise.proxy.Proxy;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * ProxyTest
 *
 * @author zido
 * @date 2018/04/28
 */
public class ProxyTest {
    @Test
    public void proxyReaderTest() throws IOException {
        InputStream stream = getClass().getResourceAsStream("/proxy.json");
        ProxyReader proxyReader = new ProxyReader(stream);
        List<Proxy> proxies = proxyReader.getProxies();
        Assert.assertEquals(5, proxies.size());
    }
}
