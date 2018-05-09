package site.zido.elise.common;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import site.zido.elise.Page;
import site.zido.elise.Request;
import site.zido.elise.Task;
import site.zido.elise.downloader.AbstractDownloader;
import site.zido.elise.proxy.Proxy;
import site.zido.elise.proxy.ProxyProvider;
import site.zido.elise.selector.PlainText;
import site.zido.elise.utils.ValidateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * HtmlUnitDownloader
 *
 * @author zido
 * @date 2018/05/03
 */
public class HtmlUnitDownloader extends AbstractDownloader {
    private ProxyProvider proxyProvider;
    private static final Logger logger = LoggerFactory.getLogger(HtmlUnitDownloader.class);

    @Override
    public Page download(Request request, Task task) {
        WebClient webClient = null;
        Proxy proxy = proxyProvider != null ? proxyProvider.getProxy(task) : null;
        Page page = Page.fail();
        try {
            if (proxy != null) {
                webClient = new WebClient(BrowserVersion.CHROME, proxy.getHost(), proxy.getPort());
            } else {
                webClient = new WebClient(BrowserVersion.CHROME);
            }
            String urlStr = request.getUrl();
            URL url = new URL(urlStr);

            WebRequest webRequest = new WebRequest(url);
            String charset = request.getCharset();
            if (!ValidateUtils.isEmpty(charset)) {
                webRequest.setCharset(Charset.forName(charset));
            }
            HtmlPage htmlPage = webClient.getPage(webRequest);
            page = new Page();
            page.setUrl(new PlainText(request.getUrl()));
            page.setRawText(htmlPage.asXml());
            page.setDownloadSuccess(true);
        } catch (MalformedURLException e) {
            logger.error(String.format("url is invalid [%s]", request.getUrl()), e);
        } catch (IOException e) {
            logger.error(String.format("download page fail [%s]", request.getUrl()), e);
        } finally {
            if (webClient != null) {
                webClient.close();
            }
        }
        return page;
    }

    @Override
    public void setThread(int threadNum) {

    }

    public HtmlUnitDownloader setProxyProvider(ProxyProvider proxyProvider) {
        this.proxyProvider = proxyProvider;
        return this;
    }
}
