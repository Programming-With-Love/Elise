package site.zido.elise.http.impl;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import site.zido.elise.http.Body;
import site.zido.elise.http.Http;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * wrapper httpclient body to elise body
 *
 * @author zido
 */
public class HttpClientBodyWrapper implements Body {
    private static final byte[] EMPTY_BYTES = new byte[0];
    private static final long serialVersionUID = -7276549416780682765L;
    private HttpEntity entity;
    private Http.ContentType contentType;
    private byte[] bytes;

    public HttpClientBodyWrapper(HttpEntity entity) {
        this.entity = entity;
        this.contentType = Http.ContentType.parse(entity.getContentType().getValue());
    }

    @Override
    public synchronized byte[] getBytes() {
        if (bytes == null) {
            try {
                bytes = EntityUtils.toByteArray(entity);
            } catch (IOException e) {
                bytes = EMPTY_BYTES;
            }
        }
        return bytes;
    }

    @Override
    public Http.ContentType getContentType() {
        return contentType;
    }

    @Override
    public Charset getEncoding() {
        Header encoding = entity.getContentEncoding();
        if (encoding != null) {
            return Charset.forName(encoding.getValue());
        }
        return Charset.forName(contentType.getCharset());
    }

}
