package site.zido.elise.http;

import site.zido.elise.select.Selectable;
import site.zido.elise.select.Text;

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
    Text getUrl();

    /**
     * get body from response
     *
     * @return body
     */
    Selectable getBody();
}
