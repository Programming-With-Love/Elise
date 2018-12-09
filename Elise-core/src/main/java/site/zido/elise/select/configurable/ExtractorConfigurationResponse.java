package site.zido.elise.select.configurable;

import site.zido.elise.http.Body;
import site.zido.elise.http.Header;
import site.zido.elise.http.Response;

import java.util.List;

public class ExtractorConfigurationResponse implements Response {
    @Override
    public int getStatusCode() {
        return 0;
    }

    @Override
    public String getReasonPhrase() {
        return null;
    }

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public Body getBody() {
        return null;
    }

    @Override
    public List<Header> getHeaders(String key) {
        return null;
    }

    @Override
    public List<Header> getAllHeaders() {
        return null;
    }
}
