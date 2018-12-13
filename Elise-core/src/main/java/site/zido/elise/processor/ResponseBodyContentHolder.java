package site.zido.elise.processor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import site.zido.elise.custom.Config;
import site.zido.elise.custom.GlobalConfig;
import site.zido.elise.http.Body;
import site.zido.elise.http.Response;

import java.nio.charset.Charset;

public class ResponseBodyContentHolder {
    private String html;
    private String url;
    private Document document;
    private byte[] bytes;
    private Charset charset;

    public ResponseBodyContentHolder(Response response, Config config) {
        Body body = response.getBody();
        this.bytes = body.getBytes();
        Charset encoding = body.getEncoding();
        String configCharset = config.get(GlobalConfig.KEY_CHARSET);
        this.charset = encoding == null ? Charset.forName(configCharset) : encoding;
        this.url = response.getUrl();
    }

    public String getHtml() {
        if (html == null) {
            html = new String(bytes, charset);
        }
        return html;
    }

    public Document getDocument() {
        if (document == null) {
            document = Jsoup.parse(html);
        }
        return document;
    }

    public byte[] getBytes() {
        return bytes;
    }

}
