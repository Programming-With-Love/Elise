package site.zido.elise.downloader.httpclient;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

/**
 * wrap site.zido.elise.Header to http client's header
 *
 * @author zido
 */
public class HttpClientHeaderWrapper extends BasicHeader implements Header {
    private static final long serialVersionUID = -8918531998903473871L;

    /**
     * Instantiates a new Http client header wrapper.
     *
     * @param header the header
     */
    public HttpClientHeaderWrapper(site.zido.elise.http.Header header) {
        super(header.getName(), header.getValue());
    }
}
