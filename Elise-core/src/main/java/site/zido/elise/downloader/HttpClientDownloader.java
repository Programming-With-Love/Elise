package site.zido.elise.downloader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.custom.HttpClientConfig;
import site.zido.elise.downloader.httpclient.HttpClientHeaderWrapper;
import site.zido.elise.http.Body;
import site.zido.elise.http.Http;
import site.zido.elise.http.Request;
import site.zido.elise.http.Response;
import site.zido.elise.http.impl.DefaultBody;
import site.zido.elise.http.impl.DefaultCookie;
import site.zido.elise.http.impl.DefaultResponse;
import site.zido.elise.http.impl.HttpClientBodyWrapper;
import site.zido.elise.task.Task;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Downloader using http client framework
 *
 * @author zido
 */
public class HttpClientDownloader implements Downloader {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientDownloader.class);
    private CloseableHttpClient client;
    private ConcurrentHashMap<Long, HttpClientContext> contextContainer = new ConcurrentHashMap<>();

    /**
     * Instantiates a new Http client downloader.
     *
     * @param client the client
     */
    public HttpClientDownloader(CloseableHttpClient client) {
        this.client = client;
    }

    @Override
    public Response download(Task task, Request request) {
        CloseableHttpResponse httpResponse = null;
        HttpClientContext context = getContext(task);
        HttpUriRequest httpUriRequest = buildRequest(task, request);
        DefaultResponse response = DefaultResponse.fail();
        try {
            httpResponse = client.execute(httpUriRequest, context);
            response = handleResponse(request, task, httpResponse, context);
            LOGGER.debug("downloading response success {}", request.getUrl());
            return response;
        } catch (IOException e) {
            LOGGER.error("download response {} error", request.getUrl(), e);
            return response;
        } finally {
            if (httpResponse != null) {
                EntityUtils.consumeQuietly(httpResponse.getEntity());
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    LOGGER.error("http response close failed", e);
                }
            }
        }
    }

    private DefaultResponse handleResponse(Request request, Task task, HttpResponse httpResponse, HttpClientContext context) {
        String contentType = httpResponse.getEntity().getContentType() == null ? "" : httpResponse.getEntity().getContentType().getValue();
        DefaultResponse response = new DefaultResponse();
        response.setContentType(Http.ContentType.parse(contentType));
        response.setBody(new HttpClientBodyWrapper(httpResponse.getEntity()));
        response.setBody(new DefaultBody());
        response.setUrl(request.getUrl());
        response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        response.setDownloadSuccess(true);
        List<Cookie> cookies = context.getCookieStore().getCookies();
        List<site.zido.elise.http.Cookie> wrapCookies = new ArrayList<>();
        for (Cookie cookie : cookies) {
            wrapCookies.add(new DefaultCookie(cookie.getName(), cookie.getValue()));
        }
        response.setCookies(wrapCookies);
        return response;
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
            final HttpClientConfig config = new HttpClientConfig(task.getConfig());
            boolean disableCookie = config.getDisableCookie();
            if (disableCookie) {
                context.setCookieSpecRegistry(name -> null);
            }
            context.setCookieStore(new BasicCookieStore());
            return context;
        });
    }

    private HttpUriRequest buildRequest(Task task, Request request) {
        RequestBuilder builder = RequestBuilder.create(request.getMethod());
        final Body body = request.getBody();
        if (body != null) {
            ByteArrayEntity bodyEntity = new ByteArrayEntity(body.getBytes());
            bodyEntity.setContentType(body.getContentType().toString());
            builder.setEntity(bodyEntity);
        }
        final HttpClientConfig config = new HttpClientConfig(task.getConfig());
        final String charset = config.getCharset();
        if (charset != null) {
            builder.setCharset(Charset.forName(charset));
        }
        builder.setUri(request.getUrl());
        if (config.getHeaders() != null) {
            for (site.zido.elise.http.Header header : config.getHeaders()) {
                builder.addHeader(new HttpClientHeaderWrapper(header));
            }
        }
        return builder.build();
    }
}
