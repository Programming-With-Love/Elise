package site.zido.elise.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Http.
 *
 * @author zido
 */
public class Http {
    private Http() {
    }

    /**
     * The type Method.
     *
     * @author zido
     */
    public static class Method {
        /**
         * The constant GET.
         */
        public static final String GET = "GET";
        /**
         * The constant HEAD.
         */
        public static final String HEAD = "HEAD";
        /**
         * The constant POST.
         */
        public static final String POST = "POST";
        /**
         * The constant PUT.
         */
        public static final String PUT = "PUT";
        /**
         * The constant DELETE.
         */
        public static final String DELETE = "DELETE";

        /**
         * The constant PATCH.
         */
        public static final String PATCH = "PATCH";

        private Method() {
        }
    }

    /**
     * 简单的content-type表示，仅包含type和charset
     * <p>
     * type已经包含了http中定义的type和subtype字段，也即是说类似'application/json'
     *
     * @author zido
     */
    public static class ContentType {
        /**
         * The constant TEXT_HTML.
         */
        public static final ContentType TEXT_HTML;
        /**
         * The constant TEXT_PLAIN.
         */
        public static final ContentType TEXT_PLAIN;
        /**
         * The constant TEXT_XML.
         */
        public static final ContentType TEXT_XML;
        /**
         * The constant IMAGE_GIF.
         */
        public static final ContentType IMAGE_GIF;
        /**
         * The constant IMAGE_JPEG.
         */
        public static final ContentType IMAGE_JPEG;
        /**
         * The constant IMAGE_PNG.
         */
        public static final ContentType IMAGE_PNG;
        /**
         * The constant APPLICATION_JSON.
         */
        public static final ContentType APPLICATION_JSON;
        /**
         * The constant APPLICATION_JSON_UTF_8.
         */
        public static final ContentType APPLICATION_JSON_UTF_8;
        /**
         * The constant APPLICATION_OCTET_STREAM.
         */
        public static final ContentType APPLICATION_OCTET_STREAM;
        /**
         * The constant APPLICATION_X_WWW_FORM_URLENCODED.
         */
        public static final ContentType APPLICATION_X_WWW_FORM_URLENCODED;
        /**
         * The constant MULTIPART_FORM_DATA.
         */
        public static final ContentType MULTIPART_FORM_DATA;
        private static final Pattern CONTENT_TYPE_PATTERN;
        private static final Pattern PATTERN_FOR_CHARSET;

        static {
            CONTENT_TYPE_PATTERN = Pattern.compile("(Content-Type\\s*:)?\\s*([^;]*)");
            PATTERN_FOR_CHARSET = Pattern.compile("(Content-Type\\s*:)?\\s*([^;]*)");
            TEXT_HTML = parse("text/html");
            TEXT_PLAIN = parse("text/plain");
            TEXT_XML = parse("text/xml");
            IMAGE_GIF = parse("image/gif");
            IMAGE_JPEG = parse("image/jpeg");
            IMAGE_PNG = parse("image/png");
            APPLICATION_JSON = parse("application/json");
            APPLICATION_JSON_UTF_8 = parse("application/json;charset:utf-8");
            APPLICATION_OCTET_STREAM = parse("application/octet-stream");
            APPLICATION_X_WWW_FORM_URLENCODED = parse("application/x-www-form-urlencoded");
            MULTIPART_FORM_DATA = parse("multipart/form-data");
        }

        private String type;
        private String charset;

        /**
         * Instantiates a new Content type.
         *
         * @param type    the type
         * @param charset the charset
         */
        public ContentType(String type, String charset) {
            this.type = type;
            this.charset = charset;
        }

        /**
         * 解析head中的Content-Type
         *
         * @param contentType head中的Content-Type字符串
         * @return ContentType content type
         */
        public static ContentType parse(String contentType) {
            String type = "*/*";
            String charset = "utf-8";
            return parse(contentType, type, charset);
        }

        /**
         * Parse content type.
         *
         * @param contentType    the content type
         * @param defaultType    the default type
         * @param defaultCharset the default charset
         * @return the content type
         */
        public static ContentType parse(String contentType, String defaultType, String defaultCharset) {
            String type = defaultType;
            String charset = defaultCharset;
            Matcher matcher = CONTENT_TYPE_PATTERN.matcher(contentType);
            if (matcher.find()) {
                type = matcher.group(2);
            }
            matcher = PATTERN_FOR_CHARSET.matcher(contentType);
            if (matcher.find()) {
                charset = matcher.group(1);
            }
            return new ContentType(type, charset);
        }

        /**
         * Gets type.
         *
         * @return the type
         */
        public String getType() {
            return type;
        }

        /**
         * Sets type.
         *
         * @param type the type
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * Gets charset.
         *
         * @return the charset
         */
        public String getCharset() {
            return charset;
        }

        /**
         * Sets charset.
         *
         * @param charset the charset
         */
        public void setCharset(String charset) {
            this.charset = charset;
        }

        @Override
        public String toString() {
            String charset = this.charset == null ? "" : "; charset:" + this.charset;
            return type + charset;
        }
    }
}
