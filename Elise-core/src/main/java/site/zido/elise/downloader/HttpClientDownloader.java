package site.zido.elise.downloader;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.http.impl.DefaultResponse;
import site.zido.elise.http.Request;
import site.zido.elise.Site;
import site.zido.elise.Task;
import site.zido.elise.http.Http;
import site.zido.elise.proxy.Proxy;
import site.zido.elise.proxy.ProxyProvider;
import site.zido.elise.select.HTML;
import site.zido.elise.select.Text;
import site.zido.elise.utils.HtmlUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Downloader using http client framework
 *
 * @author zido
 */
public class HttpClientDownloader implements Downloader {
    private final Map<String, CloseableHttpClient> httpClients = new HashMap<>();
    private Logger logger = LoggerFactory.getLogger(HttpClientDownloader.class);
    private HttpClientGenerator httpClientGenerator = new HttpClientGenerator();

    private HttpUriRequestConverter httpUriRequestConverter = new HttpUriRequestConverter();

    private ProxyProvider proxyProvider;
    private boolean responseHeader = true;

    private CloseableHttpClient getHttpClient(Site site) {
        if (site == null) {
            return httpClientGenerator.getClient(null);
        }
        String domain = site.getDomain();
        CloseableHttpClient httpClient = httpClients.get(domain);
        if (httpClient == null) {
            synchronized (this) {
                httpClient = httpClients.get(domain);
                if (httpClient == null) {
                    httpClient = httpClientGenerator.getClient(site);
                    httpClients.put(domain, httpClient);
                }
            }
        }
        return httpClient;
    }

    @Override
    public DefaultResponse download(Task task, Request request) {
        CloseableHttpResponse httpResponse = null;
        CloseableHttpClient httpClient = getHttpClient(task.getSite());
        Proxy proxy = proxyProvider != null ? proxyProvider.getProxy(task) : null;
        HttpClientRequestContext requestContext = httpUriRequestConverter.convert(request, task.getSite(), proxy);
        DefaultResponse response = DefaultResponse.fail();
        try {
            httpResponse = httpClient.execute(requestContext.getHttpUriRequest(), requestContext.getHttpClientContext());
            response = handleResponse(request, task, httpResponse);
            logger.debug("downloading response success {}", request.getUrl());
            return response;
        } catch (IOException e) {
            logger.error("download response {} error", request.getUrl(), e);
            return response;
        } finally {
            if (httpResponse != null) {
                EntityUtils.consumeQuietly(httpResponse.getEntity());
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    logger.error("http response close failed", e);
                }
            }
            if (proxyProvider != null && proxy != null) {
                proxyProvider.returnProxy(proxy, response, task);
            }
        }
    }

    private DefaultResponse handleResponse(Request request, Task task, HttpResponse httpResponse) throws IOException {
        byte[] bytes = EntityUtils.toByteArray(httpResponse.getEntity());
        String contentType = httpResponse.getEntity().getContentType() == null ? "" : httpResponse.getEntity().getContentType().getValue();

        DefaultResponse response = new DefaultResponse();
        response.setContentType(Http.ContentType.parse(contentType));
        String charset = request.getCharset();
        charset = HtmlUtils.getHtmlCharset(bytes, charset);
        if (charset == null) {
            charset = task.getSite().getCharset();
        }
        response.setBody(new HTML(new String(bytes, charset), request.getUrl()));
        response.setUrl(new Text(request.getUrl()));
        response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        response.setDownloadSuccess(true);
        return response;
    }

    private Map<String, List<String>> convertHeaders(Header[] headers) {
        Map<String, List<String>> results = new HashMap<>(16);
        for (Header header : headers) {
            List<String> list = results.computeIfAbsent(header.getName(), k -> new ArrayList<>());
            list.add(header.getValue());
        }
        return results;
    }

    @Override
    public void setThread(int thread) {
        httpClientGenerator.setPoolSize(thread);
    }

    public void setHttpClientGenerator(HttpClientGenerator generator) {
        this.httpClientGenerator = generator;
    }

    public void setHttpUriRequestConverter(HttpUriRequestConverter httpUriRequestConverter) {
        this.httpUriRequestConverter = httpUriRequestConverter;
    }

    public void setResponseHeader(boolean responseHeader) {
        this.responseHeader = responseHeader;
    }

    public void setProxyProvider(ProxyProvider proxyProvider) {
        this.proxyProvider = proxyProvider;
    }
}
