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

    /**
     * Instantiates a new Default response.
     */
    public DefaultResponse() {
        this.headers = new ArrayList<>();
    }

    /**
     * Fail default response.
     *
     * @return the default response
     */
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

    /**
     * Sets url.
     *
     * @param url the url
     * @return the url
     */
    public DefaultResponse setUrl(Text url) {
        this.url = url;
        return this;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Sets status code.
     *
     * @param statusCode the status code
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String getReasonPhrase() {
        return reasonPhrase;
    }

    /**
     * Sets reason phrase.
     *
     * @param reasonPhrase the reason phrase
     */
    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
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
     * Is download success boolean.
     *
     * @return the boolean
     */
    public boolean isDownloadSuccess() {
        return downloadSuccess;
    }

    /**
     * Sets download success.
     *
     * @param downloadSuccess the download success
     */
    public void setDownloadSuccess(boolean downloadSuccess) {
        this.downloadSuccess = downloadSuccess;
    }

    /**
     * Gets content type.
     *
     * @return the content type
     */
    public Http.ContentType getContentType() {
        return contentType;
    }

    /**
     * Sets content type.
     *
     * @param contentType the content type
     */
    public void setContentType(Http.ContentType contentType) {
        this.contentType = contentType;
    }

    /**
     * Gets body.
     *
     * @return the body
     */
    public Selectable getBody() {
        return body;
    }

    /**
     * Sets body.
     *
     * @param body the body
     */
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

    /**
     * Sets header.
     *
     * @param header the header
     */
    public void setHeader(Header header) {
        headers.add(header);
    }

}
