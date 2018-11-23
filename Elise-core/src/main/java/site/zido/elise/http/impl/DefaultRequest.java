package site.zido.elise.http.impl;

import site.zido.elise.http.Cookie;
import site.zido.elise.http.Header;
import site.zido.elise.http.HttpRequestBody;
import site.zido.elise.http.Request;

import java.util.*;
import java.util.stream.Collectors;

/**
 * DefaultRequest
 *
 * @author zido
 */
public class DefaultRequest implements Request {
    public static final String CYCLE_TRIED_TIMES = "_cycle_tried_times";
    private static final long serialVersionUID = 2018040215121L;
    /**
     * url
     */
    private String url;
    /**
     * request method,like GET/POST/PATCH/PUT/DELETE
     */
    private String method;
    /**
     * Only the three verbs POST, PUT, and PATCH will include the request body.
     * <p>
     * If other methods are used, the request body will be ignored
     */
    private HttpRequestBody body;
    /**
     * Store additional information in extras.
     */
    private Map<String, Object> extras;
    /**
     * cookies for current url, if not set use Site's cookies
     */
    private List<Cookie> cookies = new ArrayList<>();

    private List<Header> headers = new ArrayList<>();


    private String charset;

    /**
     * Priority of the request.
     * The bigger will be processed earlier.
     */
    private long priority;
    /**
     * When it is set to TRUE, the downloader will not try to parse response body to text.
     */
    private boolean binaryContent = false;

    public DefaultRequest(DefaultRequest request) {
        this.url = request.url;
        this.method = request.method;
        this.extras = request.extras;
        this.cookies = request.cookies;
        this.headers = request.headers;
        this.charset = request.charset;
        this.body = request.body;
        this.priority = request.priority;
        this.binaryContent = request.binaryContent;
    }

    public DefaultRequest() {

    }

    public DefaultRequest(String url) {
        this.url = url;
    }

    public Object getExtra(String key) {
        if (extras == null) {
            return null;
        }
        return extras.get(key);
    }

    public DefaultRequest putExtra(String key, Object value) {
        if (extras == null) {
            extras = new HashMap<>(3);
        }
        extras.put(key, value);
        return this;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DefaultRequest request = (DefaultRequest) o;

        if (url != null ? !url.equals(request.url) : request.url != null) {
            return false;
        }
        return method != null ? method.equals(request.method) : request.method == null;
    }

    public void addCookie(Cookie cookie) {
        this.cookies.add(cookie);
    }

    public DefaultRequest addHeader(Header header) {
        headers.add(header);
        return this;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public HttpRequestBody getBody() {
        return body;
    }

    public void setBody(HttpRequestBody body) {
        this.body = body;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, Object> extras) {
        this.extras = extras;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    @Override
    public List<Header> getHeaders(String key) {
        return headers.stream().filter(header -> Objects.equals(header.getName(), key)).collect(Collectors.toList());
    }

    @Override
    public List<Header> getAllHeaders() {
        return headers;
    }
}
