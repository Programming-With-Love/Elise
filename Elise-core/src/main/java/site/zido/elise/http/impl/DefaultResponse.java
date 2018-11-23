package site.zido.elise.http.impl;

import site.zido.elise.http.Header;
import site.zido.elise.http.Http;
import site.zido.elise.http.Response;
import site.zido.elise.select.Selectable;
import site.zido.elise.select.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 下载的响应对象
 *
 * @author zido
 */
public class DefaultResponse implements Response {
    private static final long serialVersionUID = 8652625484193923483L;
    private Text url;
    private int statusCode = 200;
    private String reasonPhrase;

    private Selectable body;

    private boolean downloadSuccess = true;

    private Http.ContentType contentType;

    private List<Header> headers;

    public DefaultResponse() {
        this.headers = new ArrayList<>();
    }

    public static DefaultResponse fail() {
        DefaultResponse response = new DefaultResponse();
        response.setDownloadSuccess(false);
        response.setStatusCode(-1);
        return response;
    }

    /**
     * get url of current page
     *
     * @return url of current page
     */
    @Override
    public Text getUrl() {
        return url;
    }

    public DefaultResponse setUrl(Text url) {
        this.url = url;
        return this;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public boolean isDownloadSuccess() {
        return downloadSuccess;
    }

    public void setDownloadSuccess(boolean downloadSuccess) {
        this.downloadSuccess = downloadSuccess;
    }

    public Http.ContentType getContentType() {
        return contentType;
    }

    public void setContentType(Http.ContentType contentType) {
        this.contentType = contentType;
    }

    public Selectable getBody() {
        return body;
    }

    public void setBody(Selectable body) {
        this.body = body;
    }

    @Override
    public List<Header> getHeaders(String key) {
        return headers.stream().filter(header -> Objects.equals(header.getName(), key)).collect(Collectors.toList());
    }

    @Override
    public List<Header> getAllHeaders() {
        return headers;
    }

    public void setHeader(Header header) {
        headers.add(header);
    }

}
