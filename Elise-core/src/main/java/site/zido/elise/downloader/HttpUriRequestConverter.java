package site.zido.elise.downloader;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import site.zido.elise.Request;
import site.zido.elise.Site;
import site.zido.elise.http.Http;
import site.zido.elise.proxy.Proxy;
import site.zido.elise.utils.UrlUtils;

import java.util.Map;

/**
 * Http Uri Request Converter
 *
 * @author zido
 */
public class HttpUriRequestConverter {

    public HttpClientRequestContext convert(Request request, Site site, Proxy proxy) {
        HttpClientRequestContext httpClientRequestContext = new HttpClientRequestContext();
        httpClientRequestContext.setHttpUriRequest(convertHttpUriRequest(request, site, proxy));
        httpClientRequestContext.setHttpClientContext(convertHttpClientContext(request, proxy));
        return httpClientRequestContext;
    }

    private HttpClientContext convertHttpClientContext(Request request, Proxy proxy) {
        HttpClientContext httpContext = new HttpClientContext();
        if (proxy != null && proxy.getUsername() != null) {
            AuthState authState = new AuthState();
            authState.update(new BasicScheme(ChallengeState.PROXY), new UsernamePasswordCredentials(proxy.getUsername(), proxy.getPassword()));
            httpContext.setAttribute(HttpClientContext.PROXY_AUTH_STATE, authState);
        }
        if (request.getCookies() != null && !request.getCookies().isEmpty()) {
            CookieStore cookieStore = new BasicCookieStore();
            for (Map.Entry<String, String> cookieEntry : request.getCookies().entrySet()) {
                BasicClientCookie cookie1 = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
                cookie1.setDomain(UrlUtils.removePort(UrlUtils.getDomain(request.getUrl())));
                cookieStore.addCookie(cookie1);
            }
            httpContext.setCookieStore(cookieStore);
        }
        return httpContext;
    }

    private HttpUriRequest convertHttpUriRequest(Request request, Site site, Proxy proxy) {
        RequestBuilder requestBuilder = selectRequestMethod(request).setUri(UrlUtils.fixIllegalCharacterInUrl(request.getUrl()));
        if (site.getHeaders() != null) {
            for (Map.Entry<String, String> headerEntry : site.getHeaders().entrySet()) {
                requestBuilder.addHeader(headerEntry.getKey(), headerEntry.getValue());
            }
        }

        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        requestConfigBuilder.setConnectionRequestTimeout(site.getTimeOut())
                .setSocketTimeout(site.getTimeOut())
                .setConnectTimeout(site.getTimeOut())
                .setCookieSpec(CookieSpecs.STANDARD);

        if (proxy != null) {
            requestConfigBuilder.setProxy(new HttpHost(proxy.getHost(), proxy.getPort()));
        }
        requestBuilder.setConfig(requestConfigBuilder.build());
        HttpUriRequest httpUriRequest = requestBuilder.build();
        if (request.getHeaders() != null && !request.getHeaders().isEmpty()) {
            for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
                httpUriRequest.addHeader(header.getKey(), header.getValue());
            }
        }
        return httpUriRequest;
    }

    private RequestBuilder selectRequestMethod(Request request) {
        String method = request.getMethod();
        if (method == null || method.equalsIgnoreCase(Http.Method.GET)) {
            //default get
            return RequestBuilder.get();
        } else if (method.equalsIgnoreCase(Http.Method.POST)) {
            return addFormParams(RequestBuilder.post(), request);
        } else if (method.equalsIgnoreCase(Http.Method.HEAD)) {
            return RequestBuilder.head();
        } else if (method.equalsIgnoreCase(Http.Method.PUT)) {
            return addFormParams(RequestBuilder.put(), request);
        } else if (method.equalsIgnoreCase(Http.Method.DELETE)) {
            return RequestBuilder.delete();
        } else if (method.equalsIgnoreCase(Http.Method.TRACE)) {
            return RequestBuilder.trace();
        }
        throw new IllegalArgumentException("Illegal HTTP Method " + method);
    }

    private RequestBuilder addFormParams(RequestBuilder requestBuilder, Request request) {
        if (request.getRequestBody() != null) {
            ByteArrayEntity entity = new ByteArrayEntity(request.getRequestBody().getBody());
            entity.setContentType(request.getRequestBody().getContentType().getType());
            requestBuilder.setEntity(entity);
        }
        return requestBuilder;
    }

}
