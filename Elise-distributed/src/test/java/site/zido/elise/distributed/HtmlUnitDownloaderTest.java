package site.zido.elise.distributed;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import site.zido.elise.Page;
import site.zido.elise.Request;
import site.zido.elise.Site;
import site.zido.elise.configurable.ConfigurableUrlFinder;
import site.zido.elise.downloader.AutoSwitchDownloader;
import site.zido.elise.downloader.HtmlUnitDownloader;
import site.zido.elise.selector.LinkProperty;
import site.zido.elise.selector.UrlFinderSelector;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * HtmlUnitDownloaderTest
 *
 * @author zido
 */
public class HtmlUnitDownloaderTest {
    @Test
    public void testDownload() {
        HtmlUnitDownloader downloader = new HtmlUnitDownloader();
        Request request = new Request("https://mp.weixin.qq.com/profile?src=3&timestamp=1525328049&ver=1&signature=rgiC58eFR3-iV3QPa7i3GQJ9HDU85J9JdidIdVjTUB9sKL-MoK*opYIFssI7A2Pf3d9FcPUfEamgT4SihHSovQ==");
        Page page = downloader.download(request, new Site().toTask());
        UrlFinderSelector selector = new UrlFinderSelector(new ConfigurableUrlFinder().addLinkProperty(new LinkProperty("h4", "hrefs")));
        List<String> urls = page.html().selectList(selector).all();
        Assert.assertEquals(8, urls.size());
    }

    @Test
    public void testAutoDownload() {
        AutoSwitchDownloader downloader = new AutoSwitchDownloader();
        Request request = new Request("https://mp.weixin.qq.com/profile?src=3&timestamp=1525328049&ver=1&signature=rgiC58eFR3-iV3QPa7i3GQJ9HDU85J9JdidIdVjTUB9sKL-MoK*opYIFssI7A2Pf3d9FcPUfEamgT4SihHSovQ==");
        Page page = downloader.download(request, new Site().putExtra("downloadMode", "htmlUnit").toTask());
        UrlFinderSelector selector = new UrlFinderSelector(new ConfigurableUrlFinder().addLinkProperty(new LinkProperty("h4", "hrefs")));
        List<String> urls = page.html().selectList(selector).all();
        Assert.assertEquals(8, urls.size());
    }

    @Test
    public void testProxyDownload() throws IOException {
        WebClient webClient = null;
        webClient = new WebClient(BrowserVersion.CHROME, "60.189.124.175", 61234);
        String urlStr = "http://weixin.sogou.com/weixin?type=1&s_from=input&query=%E8%80%81%E5%8F%AB%E5%85%BD&ie=utf8&_sug_=n&_sug_type_=";
        URL url = new URL(urlStr);
        WebRequest webRequest = new WebRequest(url);
        HtmlPage htmlPage = webClient.getPage(webRequest);
    }
}
