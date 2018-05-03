package com.hnqc.ironhand.common;

import com.hnqc.ironhand.Page;
import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Site;
import com.hnqc.ironhand.configurable.ConfigurableUrlFinder;
import com.hnqc.ironhand.selector.LinkProperty;
import com.hnqc.ironhand.selector.UrlFinderSelector;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * HtmlUnitDownloaderTest
 *
 * @author zido
 * @date 2018/05/03
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
}
