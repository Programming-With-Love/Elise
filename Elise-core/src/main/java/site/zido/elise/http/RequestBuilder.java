package site.zido.elise.http;

import site.zido.elise.E;
import site.zido.elise.http.impl.DefaultBody;
import site.zido.elise.http.impl.DefaultCookie;
import site.zido.elise.http.impl.DefaultHeader;
import site.zido.elise.http.impl.DefaultRequest;
import site.zido.elise.utils.Asserts;
import site.zido.elise.utils.UrlUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type Request builder.
 *
 * @author zido
 */
public class RequestBuilder {
    private String url;
    private String method;
    private Body body;
    private List<Cookie> cookies = new ArrayList<>();

    private List<Header> headers = new ArrayList<>();

    /**
     * Instantiates a new Request builder.
     *
     * @param method the method
     * @param url    the url
     */
    protected RequestBuilder(String method, String url) {
        this.url = url;
        this.method = method;
    }

    /**
     * Create request builder.
     *
     * @param method the method
     * @param url    the url
     * @return the request builder
     */
    public static RequestBuilder create(String method, String url) {
        return new RequestBuilder(method, url);
    }

    /**
     * Get request builder.
     *
     * @param url the url
     * @return the request builder
     */
    public static RequestBuilder get(String url) {
        return new RequestBuilder(Http.Method.GET, url);
    }

    /**
     * Post request builder.
     *
     * @param url the url
     * @return the request builder
     */
    public static RequestBuilder post(String url) {
        return new RequestBuilder(Http.Method.POST, url);
    }

    /**
     * Delete request builder.
     *
     * @param url the url
     * @return the request builder
     */
    public static RequestBuilder delete(String url) {
        return new RequestBuilder(Http.Method.DELETE, url);
    }

    /**
     * Put request builder.
     *
     * @param url the url
     * @return the request builder
     */
    public static RequestBuilder put(String url) {
        return new RequestBuilder(Http.Method.PUT, url);
    }

    /**
     * Patch request builder.
     *
     * @param url the url
     * @return the request builder
     */
    public static RequestBuilder patch(String url) {
        return new RequestBuilder(Http.Method.PATCH, url);
    }


    /**
     * Build request.
     *
     * @return the request
     */
    public Request build() {
        final DefaultRequest request = new DefaultRequest();
        Asserts.hasLength(url, "url can't be null");
        Asserts.hasLength(method, "method can't be null");
        request.setUrl(url);
        request.setMethod(method);
        request.setBody(body);
        request.setCookies(cookies);
        request.setExtras(null);
        request.setHeaders(headers);
        return request;
    }

    /**
     * Add cookie request builder.
     *
     * @param cookie the cookie
     * @return the request builder
     */
    public RequestBuilder addCookie(Cookie cookie) {
        this.cookies.add(cookie);
        return this;
    }

    /**
     * Add cookie request builder.
     *
     * @param name  the name
     * @param value the value
     * @return the request builder
     */
    public RequestBuilder addCookie(String name, String value) {
        this.cookies.add(new DefaultCookie(name, value));
        return this;
    }

    /**
     * Add header request builder.
     *
     * @param name  the name
     * @param value the value
     * @return the request builder
     */
    public RequestBuilder addHeader(String name, String value) {
        this.headers.add(new DefaultHeader(name, value));
        return this;
    }

    /**
     * Add header request builder.
     *
     * @param header the header
     * @return the request builder
     */
    public RequestBuilder addHeader(Header header) {
        this.headers.add(header);
        return this;
    }

    /**
     * Body form request builder.
     *
     * @param params   the params
     * @param encoding the encoding
     * @return the request builder
     */
    public RequestBuilder bodyForm(Map<String, String> params, Charset encoding) {
        List<Pair> pairs = new ArrayList<>(params.size());
        byte[] bytes = UrlUtils.encodeFormat(pairs, encoding).getBytes(encoding);
        Http.ContentType contentType = Http.ContentType.APPLICATION_X_WWW_FORM_URLENCODED;
        return bodyCustom(bytes, contentType, encoding);
    }

    /**
     * Body form request builder.
     *
     * @param params the params
     * @return the request builder
     */
    public RequestBuilder bodyForm(Map<String, String> params) {
        return bodyForm(params, E.UTF_8);
    }

    /**
     * Body form request builder.
     *
     * @param formData the form data
     * @param encoding the encoding
     * @return the request builder
     */
    public RequestBuilder bodyForm(String formData, Charset encoding) {
        return bodyCustom(UrlUtils.urlEncode(formData, encoding).getBytes(encoding), Http.ContentType.APPLICATION_X_WWW_FORM_URLENCODED, encoding);
    }

    /**
     * Body form request builder.
     *
     * @param formData the form data
     * @return the request builder
     */
    public RequestBuilder bodyForm(String formData) {
        Charset encoding = E.UTF_8;
        return bodyForm(formData, encoding);
    }

    /**
     * Body json request builder.
     *
     * @param json     the json
     * @param encoding the encoding
     * @return the request builder
     */
    public RequestBuilder bodyJson(String json, Charset encoding) {
        byte[] bytes = json.getBytes(encoding);
        Http.ContentType contentType = Http.ContentType.APPLICATION_JSON;
        return bodyCustom(bytes, contentType, encoding);
    }

    /**
     * Body json request builder.
     *
     * @param json the json
     * @return the request builder
     */
    public RequestBuilder bodyJson(String json) {
        return bodyJson(json, E.UTF_8);
    }

    /**
     * Body xml request builder.
     *
     * @param xml      the xml
     * @param encoding the encoding
     * @return the request builder
     */
    public RequestBuilder bodyXml(String xml, Charset encoding) {
        byte[] bytes = xml.getBytes(encoding);
        Http.ContentType contentType = Http.ContentType.TEXT_XML;
        return bodyCustom(bytes, contentType, encoding);
    }

    /**
     * Body xml request builder.
     *
     * @param xml the xml
     * @return the request builder
     */
    public RequestBuilder bodyXml(String xml) {
        return bodyXml(xml, E.UTF_8);
    }

    /**
     * Body custom request builder.
     *
     * @param bytes       the bytes
     * @param contentType the content type
     * @param encoding    the encoding
     * @return the request builder
     */
    public RequestBuilder bodyCustom(byte[] bytes, Http.ContentType contentType, Charset encoding) {
        DefaultBody body = new DefaultBody();
        body.setBytes(bytes);
        body.setContentType(contentType);
        body.setEncoding(encoding);
        this.body = body;
        return this;
    }


}
