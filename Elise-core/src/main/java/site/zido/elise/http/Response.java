package site.zido.elise.http;

import java.util.List;

/**
 * The interface Response.
 *
 * @author zido
 */
public interface Response extends HttpModel {
    /**
     * Gets status code.
     *
     * @return the status code
     */
    int getStatusCode();

    /**
     * Gets reason phrase.
     *
     * @return the reason phrase
     */
    String getReasonPhrase();

    /**
     * Gets url.
     *
     * @return the url
     */
    String getUrl();

    /**
     * get body from response
     *
     * @return body body
     */
    Body getBody();

    /**
     * Gets cookies.
     *
     * @return the cookies
     */
    List<Cookie> getCookies();

}
