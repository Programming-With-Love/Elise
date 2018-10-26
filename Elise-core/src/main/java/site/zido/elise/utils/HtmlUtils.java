package site.zido.elise.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtils {
    //html5 in meta
    private static final Pattern META_CHARSET_PATTERN = Pattern.compile("<[mM][eE][tT][aA][^>]*([cC][Hh][Aa][Rr][Ss][Ee][Tt][\\s]*=[\\s\\\"']*)([\\w\\d-_]*)[^>]*>");

    public static String getHtmlCharset(byte[] htmlContent) {
        String charset = Charset.defaultCharset().name();
        return getHtmlCharset(htmlContent, charset);
    }

    public static String getHtmlCharset(byte[] bytes, String defaultCharset) {
        String content = new String(bytes);
        Matcher matcher = META_CHARSET_PATTERN.matcher(content);
        if (matcher.find()) {
            return matcher.group(2);
        } else {
            String headStr = getHeadStr(content);
            Document head = Jsoup.parse(headStr);
            Elements links = head.select("meta");
            for (Element link : links) {
                String metaContent = link.attr("content");
                if (metaContent.contains("charset")) {
                    metaContent = metaContent.substring(metaContent.indexOf("charset"));
                    return metaContent.split("=")[1];
                }
            }
        }
        return StringUtils.getEncode(bytes, defaultCharset);
    }

    public static String getHeadStr(String html) {
        char[] chars = html.toCharArray();
        char[] keywords = {'h', 'e', 'a', 'd', '>'};
        boolean isStartTag = true;
        int startIndex = -1, endIndex = -1;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '<' && i < chars.length - 1) {
                if (isStartTag) {
                    int keyIndex = 0;
                    for (int j = i + 1; j < chars.length; j++) {
                        if ((keyIndex == 0 && chars[j] == ' ') || (keyIndex == 4 && chars[j] == ' ')) {
                            continue;
                        }
                        if (chars[j] == keywords[keyIndex]) {
                            keyIndex++;
                            if (keyIndex == 5) {
                                isStartTag = false;
                                startIndex = i;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                } else if (chars[i + 1] == '/') {
                    int keyIndex = 0;
                    for (int j = i + 2; j < chars.length; j++) {
                        if ((keyIndex == 0 && chars[j] == ' ') || ((keyIndex == 4) && chars[j] == ' ')) {
                            continue;
                        }
                        if (chars[j] == keywords[keyIndex]) {
                            keyIndex++;
                            if (keyIndex == 5) {
                                endIndex = j + 1;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (startIndex != -1 && endIndex != -1) {
            return html.substring(startIndex, endIndex);
        }
        return null;
    }
}
