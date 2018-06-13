package site.zido.elise.downloader;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;

/**
 * Http Client Request Context
 *
 * @author zido
 */
public class HttpClientRequestContext {

    private HttpUriRequest httpUriRequest;

    private HttpClientContext httpClientContext;

    public HttpUriRequest getHttpUriRequest() {
        return httpUriRequest;
    }

    public void setHttpUriRequest(HttpUriRequest httpUriRequest) {
        this.httpUriRequest = httpUriRequest;
    }

    public HttpClientContext getHttpClientContext() {
        return httpClientContext;
    }

    public void setHttpClientContext(HttpClientContext httpClientContext) {
        this.httpClientContext = httpClientContext;
    }

}
