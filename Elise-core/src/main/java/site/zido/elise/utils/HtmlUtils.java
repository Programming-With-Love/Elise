package site.zido.elise.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * html utils
 *
 * @author zido
 */
public class HtmlUtils {
    /**
     * the meta charset pattern
     */
    private static final Pattern META_CHARSET_PATTERN = Pattern.compile("<[mM][eE][tT][aA][^>]*([cC][Hh][Aa][Rr][Ss][Ee][Tt][\\s]*=[\\s\\\"']*)([\\w\\d-_]*)[^>]*>");
    /**
     * the key words of head
     */
    private static final char[] KEYWORDS = {'h', 'e', 'a', 'd', '>'};

    /**
     * get the charset from html
     *
     * @param htmlContent html bytes
     * @return charset name
     */
    public static String getHtmlCharset(byte[] htmlContent) {
        String charset = Charset.defaultCharset().name();
        return getHtmlCharset(htmlContent, charset);
    }

    /**
     * get the charset from html
     *
     * @param htmlContent    html bytes
     * @param defaultCharset default charset
     * @return charset name
     */
    public static String getHtmlCharset(byte[] htmlContent, String defaultCharset) {
        String content = new String(htmlContent);
        Matcher matcher = META_CHARSET_PATTERN.matcher(content);
        if (matcher.find()) {
            return matcher.group(2);
        } else {
            String headStr = getHeadStr(content);
            if (headStr == null) {
                return defaultCharset;
            }
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
        return StringUtils.getEncode(htmlContent, defaultCharset);
    }

    /**
     * get the head string from html
     *
     * @param html the html str that contains the head tag
     * @return head string
     */
    public static String getHeadStr(String html) {
        char[] chars = html.toCharArray();
        boolean isStartTag = true;
        int startIndex = -1, endIndex = -1;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '<' && i < chars.length - 1) {
                if (isStartTag) {
                    int keyIndex = 0;
                    for (int j = i + 1; j < chars.length; j++) {
                        if (keyIndex == 0 && chars[j] == ' ') {
                            continue;
                        } else if (keyIndex == 4 && chars[j] == ' ') {
                            continue;
                        }
                        if (chars[j] == KEYWORDS[keyIndex]) {
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
                        if (keyIndex == 0 && chars[j] == ' ') {
                            continue;
                        } else if ((keyIndex == 4) && chars[j] == ' ') {
                            continue;
                        }
                        if (chars[j] == KEYWORDS[keyIndex]) {
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