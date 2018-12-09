package site.zido.elise.utils;

import java.nio.charset.Charset;

/**
 * String utils.
 *
 * @author zido
 */
public class StringUtils {
    /**
     * Has length boolean.
     *
     * @param text the text
     * @return the boolean
     */
    public static boolean hasLength(String text) {
        return text != null && text.length() > 0;
    }

    /**
     * Gets encode.
     *
     * @param bytes the bytes
     * @return the encode
     */
    public static String getEncode(byte[] bytes) {
        return getEncode(bytes, Charset.defaultCharset().name());
    }

    /**
     * Gets encode.
     *
     * @param bytes          the bytes
     * @param defaultCharset the default charset
     * @return the encode
     */
    public static String getEncode(byte[] bytes, String defaultCharset) {
        String code;
        if (bytes == null || bytes.length < 2) {
            return defaultCharset;
        }
        int p = ((int) bytes[0] & 0x00ff) << 8 | ((int) bytes[1] & 0x00ff);
        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            default:
                code = defaultCharset;
        }
        return code;
    }
}
