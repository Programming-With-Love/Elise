package site.zido.elise;

import java.nio.charset.Charset;

/**
 * this class provide some constants
 *
 * @author zido
 */
public class E {
    /**
     * The constant UTF_8.
     */
    public static final Charset UTF_8 = Charset.forName("utf-8");
    /**
     * The constant ASCII.
     */
    public static final Charset ASCII = Charset.forName("US-ASCII");
    /**
     * The constant ISO_8859_1.
     */
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

    public static class StatusCode {
        public static final int CODE_DOWNLOAD_ERROR = -1;
        public static final int CODE_200 = 200;
    }

}
