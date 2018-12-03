package site.zido.elise.downloader;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.Task;
import site.zido.elise.custom.GlobalConfig;
import site.zido.elise.custom.HttpClientConfig;
import site.zido.elise.downloader.httpclient.HttpClientHeaderWrapper;
import site.zido.elise.http.Http;
import site.zido.elise.http.Request;
import site.zido.elise.http.Response;
import site.zido.elise.http.impl.DefaultResponse;
import site.zido.elise.proxy.Proxy;
import site.zido.elise.proxy.ProxyProvider;
import site.zido.elise.select.Html;
import site.zido.elise.select.Text;
import site.zido.elise.utils.HtmlUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Downloader using http client framework
 *
 * @author zido
 */
public class HttpClientDownloader implements Downloader {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientDownloader.class);
    private CloseableHttpClient client;
    private ProxyProvider proxyProvider;
    private ConcurrentHashMap<Long, HttpClientContext> contextContainer = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Long, HttpUriRequest> requestContainer = new ConcurrentHashMap<>();

    public HttpClientDownloader(CloseableHttpClient client, ProxyProvider proxyProvider) {
        this.client = client;
        this.proxyProvider = proxyProvider;
    }

    @Override
    public Response download(Task task, Request request) {
        CloseableHttpResponse httpResponse = null;
        Proxy proxy = proxyProvider != null ? proxyProvider.getProxy(task) : null;
        HttpClientContext context = getContext(task);
        HttpUriRequest httpUriRequest = buildRequest(task, request);
        DefaultResponse response = DefaultResponse.fail();
        try {
            httpResponse = client.execute(httpUriRequest, context);
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
        String charset = HtmlUtils.getHtmlCharset(bytes, task.getSite().get(GlobalConfig.KEY_CHARSET));
        response.setBody(new Html(new String(bytes, charset), request.getUrl()));
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

    /**
     * Generate a context for the task
     *
     * @param task the task
     * @return context
     */
    private HttpClientContext getContext(Task task) {
        return contextContainer.computeIfAbsent(task.getId(), key -> {
            final HttpClientContext context = HttpClientContext.create();
            final HttpClientConfig config = new HttpClientConfig(task.modelExtractor().getConfig());
            boolean disableCookie = config.getDisableCookie();
            if (disableCookie) {
                context.setCookieSpecRegistry(name -> null);
            }
            context.setCookieStore(new BasicCookieStore());
            return context;
        });
    }

    private HttpUriRequest buildRequest(Task task, Request request) {
        return requestContainer.compute(task.getId(), (key, httpUriRequest) -> {
            RequestBuilder builder;
            if (httpUriRequest != null && httpUriRequest.getMethod().equalsIgnoreCase(request.getMethod())) {
                builder = RequestBuilder.copy(httpUriRequest);
            } else {
                builder = RequestBuilder.create(request.getMethod());
            }
            final HttpClientConfig config = new HttpClientConfig(task.modelExtractor().getConfig());
            builder.setCharset(Charset.forName(config.getCharset()));
            builder.setUri(request.getUrl());
            for (site.zido.elise.http.Header header : config.getHeaders()) {
                builder.addHeader(new HttpClientHeaderWrapper(header));
            }
            return builder.build();
        });
    }

}
