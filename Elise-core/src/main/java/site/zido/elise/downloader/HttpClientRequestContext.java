package site.zido.elise.downloader;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;

/**
 * Http Client DefaultRequest Context
 *
 * @author zido
 */
public class HttpClientRequestContext {

    private HttpUriRequest httpUriRequest;

    private HttpClientContext httpClientContext;

    /**
     * Gets http uri request.
     *
     * @return the http uri request
     */
    public HttpUriRequest getHttpUriRequest() {
        return httpUriRequest;
    }

    /**
     * Sets http uri request.
     *
     * @param httpUriRequest the http uri request
     */
    public void setHttpUriRequest(HttpUriRequest httpUriRequest) {
        this.httpUriRequest = httpUriRequest;
    }

    /**
     * Gets http client context.
     *
     * @return the http client context
     */
    public HttpClientContext getHttpClientContext() {
        return httpClientContext;
    }

    /**
     * Sets http client context.
     *
     * @param httpClientContext the http client context
     */
    public void setHttpClientContext(HttpClientContext httpClientContext) {
        this.httpClientContext = httpClientContext;
    }

}
