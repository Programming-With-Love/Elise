package site.zido.elise.utils;

import com.sun.istack.internal.Nullable;

public class StringUtils {
    public static boolean hasLength(@Nullable String text) {
        return text != null && text.length() > 0;
    }
}
