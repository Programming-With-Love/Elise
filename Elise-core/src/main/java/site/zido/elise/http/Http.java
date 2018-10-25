package site.zido.elise.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
         * The constant TRACE.
         */
        public static final String TRACE = "TRACE";

        private Method() {
        }
    }

    /**
     * 简单的content-type表示，仅包含type和charset
     * <p>
     * type已经包含了http中定义的type和subtype字段，也即是说类似'application/json'
     */
    public static class ContentType {
        public static final ContentType TEXT_HTML = parse("text/html");
        public static final ContentType TEXT_PLAIN = parse("text/plain");
        public static final ContentType TEXT_XML = parse("text/xml");
        public static final ContentType IMAGE_GIF = parse("image/gif");
        public static final ContentType IMAGE_JPEG = parse("image/jpeg");
        public static final ContentType IMAGE_PNG = parse("image/png");
        public static final ContentType APPLICATION_JSON = parse("application/json");
        public static final ContentType APPLICATION_JSON_UTF_8 = parse("application/json;charset:utf-8");
        public static final ContentType APPLICATION_OCTET_STREAM = parse("application/octet-stream");
        public static final ContentType APPLICATION_X_WWW_FORM_URLENCODED = parse("application/x-www-form-urlencoded");
        public static final ContentType MULTIPART_FORM_DATA = parse("multipart/form-data");
        private static final Pattern CONTENT_TYPE_PATTERN = Pattern.compile("(Content-Type\\s*:)?\\s*([^;]*)");
        private static final Pattern PATTERN_FOR_CHARSET = Pattern.compile("charset\\s*=\\s*['\"]*([^\\s;'\"]*)", Pattern.CASE_INSENSITIVE);
        private String type;
        private String charset;

        /**
         * 解析head中的Content-Type
         *
         * @param contentType head中的Content-Type字符串
         * @return ContentType
         */
        public static ContentType parse(String contentType) {
            String type = "*/*";
            String charset = "utf-8";
            return parse(contentType, type, charset);
        }

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

        public ContentType(String type, String charset) {
            this.type = type;
            this.charset = charset;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCharset() {
            return charset;
        }

        public void setCharset(String charset) {
            this.charset = charset;
        }
    }
}
