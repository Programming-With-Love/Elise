package site.zido.elise.http;

import site.zido.elise.select.Selectable;
import site.zido.elise.select.Text;

import java.io.Serializable;

/**
 * 下载的响应对象
 * <br>
 * 重点关注几个常用的响应内容，并未将所有的响应内容全部包含在内
 *
 * @author zido
 */
public class Response implements Serializable {
    private static final long serialVersionUID = 8652625484193923483L;
    private Text url;
    private int statusCode = 200;

    private Selectable body;

    private boolean downloadSuccess = true;

    private Http.ContentType contentType;

    public Response() {
    }

    public static Response fail() {
        Response response = new Response();
        response.setDownloadSuccess(false);
        response.setStatusCode(-1);
        return response;
    }

    /**
     * get url of current page
     *
     * @return url of current page
     */
    public Text getUrl() {
        return url;
    }

    public Response setUrl(Text url) {
        this.url = url;
        return this;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
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

    @Override
    public String toString() {
        return "Response{" +
                "url='" + url + '\'' +
                ", statusCode=" + statusCode +
                ", body=" + body +
                ", downloadSuccess=" + downloadSuccess +
                '}';
    }

    public Selectable getBody() {
        return body;
    }

    public void setBody(Selectable body) {
        this.body = body;
    }
}
