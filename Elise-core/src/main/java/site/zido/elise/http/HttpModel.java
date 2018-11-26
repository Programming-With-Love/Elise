package site.zido.elise.http;

import java.io.Serializable;
import java.util.List;

/**
 * The interface Http model.
 *
 * @author zido
 */
public interface HttpModel extends Serializable {
    /**
     * Gets headers.
     *
     * @param key the key
     * @return the headers
     */
    List<Header> getHeaders(String key);

    /**
     * Gets all headers.
     *
     * @return the all headers
     */
    List<Header> getAllHeaders();

}
