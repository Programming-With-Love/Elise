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

    /**
     * Instantiates a new Default request.
     */
    public DefaultRequest() {

    }

    /**
     * Instantiates a new Default request.
     *
     * @param url the url
     */
    public DefaultRequest(String url) {
        this.url = url;
    }

    /**
     * Gets extra.
     *
     * @param key the key
     * @return the extra
     */
    @Override
    public Object getExtra(String key) {
        if (extras == null) {
            return null;
        }
        return extras.get(key);
    }

    /**
     * Put extra default request.
     *
     * @param key   the key
     * @param value the value
     * @return the default request
     */
    @Override
    public void putExtra(String key, Object value) {
        if (extras == null) {
            extras = new HashMap<>(3);
        }
        extras.put(key, value);
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

    /**
     * Add cookie.
     *
     * @param cookie the cookie
     */
    public void addCookie(Cookie cookie) {
        this.cookies.add(cookie);
    }

    /**
     * Add header default request.
     *
     * @param header the header
     * @return the default request
     */
    public DefaultRequest addHeader(Header header) {
        headers.add(header);
        return this;
    }

    @Override
    public String getUrl() {
        return url;
    }

    /**
     * Sets url.
     *
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getMethod() {
        return method;
    }

    /**
     * Sets method.
     *
     * @param method the method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public HttpRequestBody getBody() {
        return body;
    }

    /**
     * Sets body.
     *
     * @param body the body
     */
    public void setBody(HttpRequestBody body) {
        this.body = body;
    }

    /**
     * Gets extras.
     *
     * @return the extras
     */
    public Map<String, Object> getExtras() {
        return extras;
    }

    /**
     * Sets extras.
     *
     * @param extras the extras
     */
    public void setExtras(Map<String, Object> extras) {
        this.extras = extras;
    }

    @Override
    public List<Cookie> getCookies() {
        return cookies;
    }

    /**
     * Sets cookies.
     *
     * @param cookies the cookies
     */
    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }

    @Override
    public List<Header> getHeaders() {
        return headers;
    }

    /**
     * Sets headers.
     *
     * @param headers the headers
     */
    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    /**
     * Gets charset.
     *
     * @return the charset
     */
    public String getCharset() {
        return charset;
    }

    /**
     * Sets charset.
     *
     * @param charset the charset
     */
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
