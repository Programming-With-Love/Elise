package site.zido.elise.http;

import java.util.Date;

/**
 * The interface Cookie.
 *
 * @author zido
 */
public interface Cookie extends Header {
    /**
     * Gets path.
     *
     * @return the path
     */
    String getPath();

    /**
     * Gets expiry date.
     *
     * @return the expiry date
     */
    Date getExpiryDate();

    /**
     * Is secure boolean.
     *
     * @return the boolean
     */
    boolean isSecure();

    /**
     * Gets domain.
     *
     * @return the domain
     */
    String getDomain();
}
