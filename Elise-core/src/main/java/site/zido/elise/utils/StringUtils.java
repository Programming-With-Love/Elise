package site.zido.elise.utils;

import com.sun.istack.internal.Nullable;

public class StringUtils {
    public static boolean hasLength(@Nullable String text) {
        return text != null && text.length() > 0;
    }

    public static String getEncode(byte[] bytes) {
        String code;
        if (bytes == null || bytes.length < 2) {
            return null;
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
                code = "GBK";
        }
        return code;
    }
}
