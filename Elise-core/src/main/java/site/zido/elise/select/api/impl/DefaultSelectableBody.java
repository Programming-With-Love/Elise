package site.zido.elise.select.api.impl;

import org.jsoup.Jsoup;
import site.zido.elise.http.Body;
import site.zido.elise.select.api.SelectableBody;
import site.zido.elise.select.api.body.Bytes;
import site.zido.elise.select.api.body.Html;

import java.io.IOException;
import java.util.regex.Pattern;

public class DefaultSelectableBody implements SelectableBody {
    private static final Pattern XML_LIKED_PATTERN = Pattern.compile("(html)|(xml)", Pattern.CASE_INSENSITIVE);
    private Body body;
    private String url;
    private boolean isHtml;

    public DefaultSelectableBody(Body body, String url) {
        this.body = body;
        this.url = url;
        isHtml = judgeHtml();
    }

    private boolean judgeHtml() {
        return XML_LIKED_PATTERN.matcher(body.getContentType().getType()).find();
    }

    @Override
    public Html html() {
        if (isHtml) {
            try {
                return new DefaultHtml(Jsoup.parse(body.getInputStream(), body.getEncoding().name(), url));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return DefaultHtml.EMPTY_HTML;
    }

    @Override
    public Bytes bytes() {
        return new DefaultBytes(body.getBytes());
    }

}
