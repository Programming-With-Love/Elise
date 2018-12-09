package site.zido.elise.http.impl;

import site.zido.elise.http.Http;
import site.zido.elise.http.RequestBody;

import java.nio.charset.Charset;

/**
 * The type Default request body.
 *
 * @author zido
 */
public class DefaultRequestBody implements RequestBody {
    private static final long serialVersionUID = 2018040215121L;
    private byte[] bytes;
    private Http.ContentType contentType;
    private Charset encoding;

    /**
     * Instantiates a new Http request bytes.
     */
    public DefaultRequestBody() {
    }

    /**
     * Get bytes byte [ ].
     *
     * @return the byte [ ]
     */
    @Override
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * Sets bytes.
     *
     * @param bytes the bytes
     */
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * Gets content type.
     *
     * @return the content type
     */
    @Override
    public Http.ContentType getContentType() {
        return contentType;
    }

    /**
     * Sets content type.
     *
     * @param contentType the content type
     */
    public void setContentType(Http.ContentType contentType) {
        this.contentType = contentType;
    }

    /**
     * Gets encoding.
     *
     * @return the encoding
     */
    @Override
    public Charset getEncoding() {
        return encoding;
    }

    /**
     * Sets encoding.
     *
     * @param encoding the encoding
     */
    public void setEncoding(Charset encoding) {
        this.encoding = encoding;
    }
}
