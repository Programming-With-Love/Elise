package site.zido.elise.downloader.httpclient;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

public class HttpClientHeaderWrapper extends BasicHeader implements Header {
    public HttpClientHeaderWrapper(site.zido.elise.http.Header header) {
        super(header.getName(), header.getValue());
    }
}
