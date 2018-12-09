package site.zido.elise.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The lightest sync Http tool
 *
 * @author zido
 */
public class SimpleHttpClient {
    /**
     * The constant CONTENT_TYPE.
     */
    public static final String CONTENT_TYPE = "Content-Type";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final SimpleHttpClient DEFAULT_HTTP;
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleHttpClient.class);

    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        DEFAULT_HTTP = new SimpleHttpClient();
    }

    private URL url;
    private String requestParams;
    private String httpMethod = "GET";
    private String outCharsetName = "UTF-8";
    private String inCharsetName = "UTF-8";
    private String contentType = "application/json";
    private Map<String, Map<String, String>> cookie = new HashMap<>();
    private boolean holdCookie = true;

    /**
     * Builder http builder.
     *
     * @return the http builder
     */
    public static HttpBuilder builder() {
        return new HttpBuilder();
    }

    /**
     * Send http result.
     *
     * @return the http result
     */
    public HttpResult send() {
        HttpURLConnection currentConnection = null;
        try {
            currentConnection = (HttpURLConnection) url.openConnection();
            if (requestParams != null) {
                currentConnection.setDoOutput(true);
            }
            currentConnection.setUseCaches(false);
            currentConnection.setRequestProperty("Content-Type", contentType);
            currentConnection.setRequestMethod(httpMethod);
            currentConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            currentConnection.setInstanceFollowRedirects(false);
            if (!cookie.isEmpty()) {
                StringBuilder cookieBuilder = new StringBuilder();
                for (Map.Entry<String, Map<String, String>> entry : cookie.entrySet()) {
                    Map<String, String> value = entry.getValue();
                    cookieBuilder.append(entry.getKey()).append("=").append(value.get("value")).append(";");
                }
                if (cookieBuilder.length() > 0) {
                    cookieBuilder.substring(0, cookieBuilder.length() - 1);
                    currentConnection.setRequestProperty("Cookie", cookieBuilder.toString());
                }
            }
            if (requestParams != null) {
                try (OutputStreamWriter out = new OutputStreamWriter(currentConnection.getOutputStream(), outCharsetName)) {
                    out.write(requestParams);
                    out.flush();
                }
            }
        } catch (IOException e) {
            throw new HttpWrapperException(e);
        } finally {
            if (currentConnection != null) {
                currentConnection.disconnect();
            }
        }
        Map<String, List<String>> fields = currentConnection.getHeaderFields();
        if (holdCookie) {
            List<String> cookies = fields.get("Set-Cookie");
            if (cookies != null) {
                for (String s : cookies) {
                    String[] split = s.split("[=;]");
                    Map<String, String> value = new HashMap<>();
                    cookie.put(split[0], value);
                    value.put("value", split[1]);
                    for (int i = 2; i < split.length; i += 2) {
                        value.put(split[i], split[i + 1]);
                    }
                }
            }
        }

        try {
            return new HttpResult(this, currentConnection.getInputStream(), fields, currentConnection.getResponseCode());
        } catch (IOException e) {
            LOGGER.warn("send http error:url[" + url + "]", e);
            return HttpResult.fail();
        }
    }

    /**
     * Change http builder.
     *
     * @return the http builder
     */
    public HttpBuilder change() {
        return new HttpBuilder(this);
    }

    /**
     * The enum Http method.
     *
     * @author zido
     */
    public enum HTTPMethod {
        /**
         * Post http method.
         */
        POST,
        /**
         * Get http method.
         */
        GET,
        /**
         * Delete http method.
         */
        DELETE,
        /**
         * Patch http method.
         */
        PATCH
    }

    /**
     * The enum Content type.
     *
     * @author zido
     */
    public enum ContentType {
        /**
         * Json content type.
         */
        JSON("application/json"),
        /**
         * Form content type.
         */
        FORM("application/x-www-form-urlencoded");
        private String value;

        ContentType(String value) {
            this.value = value;
        }
    }

    /**
     * The type Http exception wrapper.
     *
     * @author zido
     */
    public static class HttpWrapperException extends RuntimeException {
        private static final long serialVersionUID = -5766019000294865930L;

        /**
         * Instantiates a new Http exception wrapper.
         *
         * @param e the e
         */
        public HttpWrapperException(Throwable e) {
            super(e);
        }
    }

    /**
     * The type Http builder.
     *
     * @author zido
     */
    public static class HttpBuilder {
        private SimpleHttpClient http;
        private Map<String, Object> paramMap = new HashMap<>(6);
        private ContentType contentType;

        /**
         * Instantiates a new Http builder.
         */
        public HttpBuilder() {
            this.http = new SimpleHttpClient();
        }

        /**
         * Instantiates a new Http builder.
         *
         * @param http the http
         */
        public HttpBuilder(SimpleHttpClient http) {
            this.http = http;
        }

        /**
         * Url http builder.
         *
         * @param url the url
         * @return the http builder
         */
        public HttpBuilder url(String url) {
            try {
                http.url = new URL(url);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            return this;
        }

        /**
         * Method http builder.
         *
         * @param method the method
         * @return the http builder
         */
        public HttpBuilder method(HTTPMethod method) {
            http.httpMethod = method.name();
            return this;
        }

        /**
         * Body http builder.
         *
         * @param key   the key
         * @param value the value
         * @return the http builder
         */
        public HttpBuilder body(String key, Object value) {
            paramMap.put(key, value);
            return this;
        }

        /**
         * Content type http builder.
         *
         * @param contentType the content type
         * @return the http builder
         */
        public HttpBuilder contentType(ContentType contentType) {
            this.contentType = contentType;
            return this;
        }

        /**
         * Out charset http builder.
         *
         * @param charsetName the charset name
         * @return the http builder
         */
        public HttpBuilder outCharset(String charsetName) {
            http.outCharsetName = charsetName;
            return this;
        }

        /**
         * In charset http builder.
         *
         * @param charsetName the charset name
         * @return the http builder
         */
        public HttpBuilder inCharset(String charsetName) {
            http.inCharsetName = charsetName;
            return this;
        }

        /**
         * Hold cookie http builder.
         *
         * @param holdCookie the hold cookie
         * @return the http builder
         */
        public HttpBuilder holdCookie(boolean holdCookie) {
            http.holdCookie = holdCookie;
            return this;
        }

        /**
         * Build simple http client.
         *
         * @return the simple http client
         */
        public SimpleHttpClient build() {
            if (contentType == null) {
                contentType = ContentType.JSON;
            }
            http.contentType = contentType.value;
            String requestParams = null;
            if (paramMap != null && !paramMap.isEmpty()) {
                switch (contentType) {
                    case FORM:
                        StringBuilder builder = new StringBuilder();
                        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                            builder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                        }
                        requestParams = builder.substring(0, builder.length() - 1);
                        break;
                    case JSON:
                        try {
                            requestParams = MAPPER.writeValueAsString(paramMap);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    default:
                }
            }
            http.requestParams = requestParams;
            return http;
        }

        /**
         * Clear body http builder.
         *
         * @return the http builder
         */
        public HttpBuilder clearBody() {
            paramMap.clear();
            http.requestParams = null;
            return this;
        }
    }

    /**
     * The type Http result.
     *
     * @author zido
     */
    public static class HttpResult {
        private InputStream is;
        private Map<String, List<String>> header;
        private int code;
        private String body;
        private SimpleHttpClient http;

        /**
         * Instantiates a new Http result.
         *
         * @param http   the http
         * @param is     the is
         * @param header the header
         * @param code   the code
         */
        public HttpResult(SimpleHttpClient http, InputStream is, Map<String, List<String>> header, int code) {
            this.is = is;
            this.header = header;
            this.code = code;
            this.http = http;
        }

        /**
         * Fail http result.
         *
         * @return the http result
         */
        public static HttpResult fail() {
            return new HttpResult(null, null, null, -1);
        }

        /**
         * Success boolean.
         *
         * @return the boolean
         */
        public boolean success() {
            return code >= 200 && code < 400;
        }

        /**
         * Code int.
         *
         * @return the int
         */
        public int code() {
            return code;
        }

        /**
         * Cookie map.
         *
         * @return the map
         */
        public Map<String, Map<String, String>> cookie() {
            return http.cookie;
        }

        /**
         * Cookie string.
         *
         * @param key the key
         * @return the string
         */
        public String cookie(String key) {
            Map<String, String> map = http.cookie.get(key);
            if (map != null) {
                return map.get("value");
            }
            return null;
        }

        /**
         * Body string.
         *
         * @return the string
         * @throws IOException the io exception
         */
        public String body() throws IOException {
            if (body != null) {
                return body;
            }
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, http.outCharsetName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            body = sb.toString();
            return body;
        }

        /**
         * Gets header.
         *
         * @return the header
         */
        public Map<String, List<String>> getHeader() {
            return header;
        }
    }
}

