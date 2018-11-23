package site.zido.elise.http;

import java.util.List;

public interface Request extends HttpModel {
    String getMethod();

    String getUrl();

    HttpRequestBody getBody();

    List<Header> getHeaders();
}
