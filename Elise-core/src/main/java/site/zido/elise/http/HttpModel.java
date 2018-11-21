package site.zido.elise.http;

import org.apache.http.Header;

public interface HttpModel {
    HttpHeader[] getHeaders(String key);

    HttpHeader[] getAllHeaders();

    void setHeader(Header header);

    void addHeader(String key, String value);
}
