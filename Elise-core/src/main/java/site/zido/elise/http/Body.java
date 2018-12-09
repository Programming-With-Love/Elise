package site.zido.elise.http;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * http request
 *
 * @author zido
 */
public interface Body extends Serializable {
    /**
     * Get bytes byte [ ].
     *
     * @return the byte [ ]
     */
    byte[] getBytes();

    /**
     * Gets content type.
     *
     * @return the content type
     */
    Http.ContentType getContentType();


    /**
     * Gets encoding.
     *
     * @return the encoding
     */
    Charset getEncoding();

}
