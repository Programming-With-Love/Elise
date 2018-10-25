package site.zido.elise.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtils {
    private static final Pattern pattern = Pattern.compile("<[mM][eE][tT][aA][^>]*([cC][Hh][Aa][Rr][Ss][Ee][Tt][\\s]*=[\\s\\\"']*)([\\w\\d-_]*)[^>]*>");

    public static String getHtmlCharset(byte[] bytes) {
        String content = new String(bytes);
        String charset;
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            charset = matcher.group(2);
        } else {
            charset = StringUtils.getEncode(bytes);
        }

        return charset;
    }
}
