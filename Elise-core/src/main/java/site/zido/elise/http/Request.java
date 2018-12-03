package site.zido.elise.http;

import java.util.List;

/**
 * The interface Request.
 *
 * @author zido
 */
public interface Request extends HttpModel {
    /**
     * The constant CYCLE_TRIED_TIMES.
     */
    String CYCLE_TRIED_TIMES = "_cycle_tried_times";

    /**
     * Gets extra.
     *
     * @param key the key
     * @return the extra
     */
    Object getExtra(String key);

    /**
     * Gets method.
     *
     * @return the method
     */
    String getMethod();

    /**
     * Gets url.
     *
     * @return the url
     */
    String getUrl();

    /**
     * Gets body.
     *
     * @return the body
     */
    HttpRequestBody getBody();

    /**
     * Gets headers.
     *
     * @return the headers
     */
    List<Header> getHeaders();

    /**
     * Gets cookies.
     *
     * @return the cookies
     */
    List<Cookie> getCookies();

    void putExtra(String key, Object value);
}
