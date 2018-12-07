package site.zido.elise.http;

import site.zido.elise.E;
import site.zido.elise.http.impl.DefaultCookie;
import site.zido.elise.http.impl.DefaultHeader;
import site.zido.elise.http.impl.DefaultRequest;
import site.zido.elise.http.impl.DefaultRequestBody;
import site.zido.elise.utils.Asserts;
import site.zido.elise.utils.UrlUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RequestBuilder {
    private String url;
    private String method;
    private RequestBody body;
    private List<Cookie> cookies = new ArrayList<>();

    private List<Header> headers = new ArrayList<>();

    protected RequestBuilder(String method, String url) {
        this.url = url;
        this.method = method;
    }

    public static RequestBuilder create(String method, String url) {
        return new RequestBuilder(method, url);
    }

    public static RequestBuilder get(String url) {
        return new RequestBuilder(Http.Method.GET, url);
    }

    public static RequestBuilder post(String url) {
        return new RequestBuilder(Http.Method.POST, url);
    }

    public static RequestBuilder delete(String url) {
        return new RequestBuilder(Http.Method.DELETE, url);
    }

    public static RequestBuilder put(String url) {
        return new RequestBuilder(Http.Method.PUT, url);
    }

    public static RequestBuilder patch(String url) {
        return new RequestBuilder(Http.Method.PATCH, url);
    }


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

    public RequestBuilder addCookie(Cookie cookie) {
        this.cookies.add(cookie);
        return this;
    }

    public RequestBuilder addCookie(String name, String value) {
        this.cookies.add(new DefaultCookie(name, value));
        return this;
    }

    public RequestBuilder addHeader(String name, String value) {
        this.headers.add(new DefaultHeader(name, value));
        return this;
    }

    public RequestBuilder addHeader(Header header) {
        this.headers.add(header);
        return this;
    }

    public RequestBuilder bodyForm(Map<String, String> params, Charset encoding) {
        List<Pair> pairs = new ArrayList<>(params.size());
        byte[] bytes = UrlUtils.encodeFormat(pairs, encoding).getBytes(encoding);
        Http.ContentType contentType = Http.ContentType.APPLICATION_X_WWW_FORM_URLENCODED;
        return bodyCustom(bytes, contentType, encoding);
    }

    public RequestBuilder bodyForm(Map<String, String> params) {
        return bodyForm(params, E.UTF_8);
    }

    public RequestBuilder bodyForm(String formData, Charset encoding) {
        return bodyCustom(UrlUtils.urlEncode(formData, encoding).getBytes(encoding), Http.ContentType.APPLICATION_X_WWW_FORM_URLENCODED, encoding);
    }

    public RequestBuilder bodyForm(String formData) {
        Charset encoding = E.UTF_8;
        return bodyForm(formData, encoding);
    }

    public RequestBuilder bodyJson(String json, Charset encoding) {
        byte[] bytes = json.getBytes(encoding);
        Http.ContentType contentType = Http.ContentType.APPLICATION_JSON;
        return bodyCustom(bytes, contentType, encoding);
    }

    public RequestBuilder bodyJson(String json) {
        return bodyJson(json, E.UTF_8);
    }

    public RequestBuilder bodyXml(String xml, Charset encoding) {
        byte[] bytes = xml.getBytes(encoding);
        Http.ContentType contentType = Http.ContentType.TEXT_XML;
        return bodyCustom(bytes, contentType, encoding);
    }

    public RequestBuilder bodyXml(String xml) {
        return bodyXml(xml, E.UTF_8);
    }

    public RequestBuilder bodyCustom(byte[] bytes, Http.ContentType contentType, Charset encoding) {
        DefaultRequestBody body = new DefaultRequestBody();
        body.setBytes(bytes);
        body.setContentType(contentType);
        body.setEncoding(encoding);
        this.body = body;
        return this;
    }


}
