package site.zido.elise;

public class EliseResponse {
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_PLAIN = "text/plain";
    public static final String CONTENT_TYPE_HTML = "text/html";
    public static final String CONTENT_TYPE_OCTET_STREAM = "application/octet-stream";

    public enum ContentType {
        JSON(CONTENT_TYPE_JSON),
        FORM(CONTENT_TYPE_X_WWW_FORM_URLENCODED);
        private String value;

        ContentType(String value) {
            this.value = value;
        }
    }
}
