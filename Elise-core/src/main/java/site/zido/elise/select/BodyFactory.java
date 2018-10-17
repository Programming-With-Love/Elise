package site.zido.elise.select;

import site.zido.elise.EliseResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BodyFactory {
    private static final Pattern CONTENT_TYPE_PATTERN = Pattern.compile("Content-Type\\s*:\\s*([^;]*)");

    public static Selectable createBody(String body, String contentType) {
        Matcher matcher = CONTENT_TYPE_PATTERN.matcher(contentType);
        if (matcher.find()) {
            contentType = matcher.group(1);
        } else {
            contentType = EliseResponse.CONTENT_TYPE_PLAIN;
        }
        switch (contentType) {
            case EliseResponse.CONTENT_TYPE_PLAIN:
            default:
                return new Text(body);
        }
    }
}
