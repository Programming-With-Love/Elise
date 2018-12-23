package site.zido.elise.processor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import site.zido.elise.custom.Config;
import site.zido.elise.custom.GlobalConfig;
import site.zido.elise.http.Body;
import site.zido.elise.http.Response;
import site.zido.elise.http.impl.DefaultResponse;

import java.nio.charset.Charset;

public class ResponseContextHolder extends DefaultResponse {
    private String html;
    private Document document;
    private Charset charset;

    public ResponseContextHolder(Response response, Config config) {
        super(response);
        Body body = response.getBody();
        if(body != null) {
            Charset encoding = body.getEncoding();
            String configCharset = config.get(GlobalConfig.KEY_CHARSET);
            this.charset = encoding == null ? Charset.forName(configCharset) : encoding;
        }
    }

    public String getHtml() {
        if (getBody() == null) {
            this.html = "";
            return "";
        }
        if (html == null) {
            html = new String(getBody().getBytes(), charset);
        }
        return html;
    }

    public Document getDocument() {
        if(html == null){
            getHtml();
        }
        if (document == null) {
            String url = getUrl();
            document = Jsoup.parse(html, url);
        }
        return document;
    }

}
