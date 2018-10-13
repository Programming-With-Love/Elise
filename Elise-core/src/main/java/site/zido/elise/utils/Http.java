package site.zido.elise.utils;

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
public class Http {
    private static final ObjectMapper mapper = new ObjectMapper();

    private static final Http DEFAULT_HTTP;
    private static final Logger LOGGER = LoggerFactory.getLogger(Http.class);

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        DEFAULT_HTTP = new Http();
    }

    private URL url;
    private String requestParams;
    private String httpMethod = "GET";
    private String outCharsetName = "UTF-8";
    private String inCharsetName = "UTF-8";
    private String contentType = "application/json";
    private Map<String, Map<String, String>> cookie = new HashMap<>();
    private boolean holdCookie = true;

    public static HttpBuilder builder() {
        return new HttpBuilder();
    }

    public static HTTPResult get(String url) {
        return DEFAULT_HTTP.change().url(url).method(HTTPMethod.GET).clearBody().holdCookie(false).build().send();
    }

    public static HTTPResult post(String url, Map<String, Object> paramMap, ContentType contentType) {
        HttpBuilder builder = DEFAULT_HTTP.change().method(HTTPMethod.POST).url(url).clearBody().contentType(contentType).holdCookie(false);
        builder.paramMap = paramMap;
        return builder.build().send();
    }

    public static HTTPResult delete(String url) {
        return DEFAULT_HTTP.change().url(url).method(HTTPMethod.DELETE).holdCookie(false).clearBody().build().send();
    }

    public static HTTPResult patch(String url, Map<String, Object> paramMap, ContentType contentType) {
        HttpBuilder builder = DEFAULT_HTTP.change().method(HTTPMethod.PATCH).url(url).clearBody().contentType(contentType).holdCookie(false);
        builder.paramMap = paramMap;
        return builder.build().send();
    }

    public HTTPResult send() {
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
            throw new HttpExceptionWrapper(e);
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
            return new HTTPResult(this, currentConnection.getInputStream(), fields, currentConnection.getResponseCode());
        } catch (IOException e) {
            LOGGER.warn("send http error:url[" + url + "]", e);
            return HTTPResult.fail();
        }
    }

    public HttpBuilder change() {
        return new HttpBuilder(this);
    }

    public enum HTTPMethod {
        POST, GET, DELETE, PATCH
    }

    public enum ContentType {
        JSON("application/json"),
        FORM("application/x-www-form-urlencoded");
        private String value;

        ContentType(String value) {
            this.value = value;
        }
    }

    public static class HttpExceptionWrapper extends RuntimeException {
        public HttpExceptionWrapper(Throwable e) {
            super(e);
        }
    }

    public static class HttpBuilder {
        private Http http;
        private Map<String, Object> paramMap = new HashMap<>(6);
        private ContentType contentType;

        public HttpBuilder() {
            this.http = new Http();
        }

        public HttpBuilder(Http http) {
            this.http = http;
        }

        public HttpBuilder url(String url) {
            try {
                http.url = new URL(url);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            return this;
        }

        public HttpBuilder method(HTTPMethod method) {
            http.httpMethod = method.name();
            return this;
        }

        public HttpBuilder body(String key, Object value) {
            paramMap.put(key, value);
            return this;
        }

        public HttpBuilder contentType(ContentType contentType) {
            this.contentType = contentType;
            return this;
        }

        public HttpBuilder outCharset(String charsetName) {
            http.outCharsetName = charsetName;
            return this;
        }

        public HttpBuilder inCharset(String charsetName) {
            http.inCharsetName = charsetName;
            return this;
        }

        public HttpBuilder holdCookie(boolean holdCookie) {
            http.holdCookie = holdCookie;
            return this;
        }

        public Http build() {
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
                            requestParams = mapper.writeValueAsString(paramMap);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                }
            }
            http.requestParams = requestParams;
            return http;
        }

        public HttpBuilder clearBody() {
            paramMap.clear();
            http.requestParams = null;
            return this;
        }
    }

    public static class HTTPResult {
        private InputStream is;
        private Map<String, List<String>> header;
        private int code;
        private String body;
        private Http http;

        public HTTPResult(Http http, InputStream is, Map<String, List<String>> header, int code) {
            this.is = is;
            this.header = header;
            this.code = code;
            this.http = http;
        }

        public static HTTPResult fail() {
            return new HTTPResult(null, null, null, -1);
        }

        public boolean success() {
            return code >= 200 && code < 400;
        }

        public int code() {
            return code;
        }

        public Map<String, Map<String, String>> cookie() {
            return http.cookie;
        }

        public String cookie(String key) {
            Map<String, String> map = http.cookie.get(key);
            if (map != null)
                return map.get("value");
            return null;
        }

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

        public Map<String, List<String>> getHeader() {
            return header;
        }
    }
}

