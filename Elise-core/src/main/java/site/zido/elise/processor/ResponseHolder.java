package site.zido.elise.processor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import site.zido.elise.custom.Config;
import site.zido.elise.http.Response;

import java.nio.charset.Charset;

public class ResponseHolder {
    private String html;
    private Document document;
    private String url;
    private byte[] bytes;
    private Charset charset;

    public static ResponseHolder create(String url, byte[] bytes, Charset charset) {
        ResponseHolder holder = new ResponseHolder();
        holder.url = url;
        holder.bytes = bytes;
        holder.charset = charset;
        return holder;
    }

    public static ResponseHolder create(Response response, Config config) {
        return null;
    }

    public String getHtml() {
        if (html == null) {
            html = new String(bytes, charset);
        }
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Document getDocument() {
        if (document == null) {
            document = Jsoup.parse(getHtml(), url);
        }
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }
}
