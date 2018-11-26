package site.zido.elise.http;

import java.io.Serializable;

/**
 * The interface Header.
 *
 * @author zido
 */
public interface Header extends Serializable {
    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets value.
     *
     * @return the value
     */
    String getValue();
}
