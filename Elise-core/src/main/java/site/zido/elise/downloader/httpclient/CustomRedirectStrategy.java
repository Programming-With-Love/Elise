package site.zido.elise.downloader.httpclient;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.http.Http;

import java.net.URI;

/**
 * 支持post 302跳转策略实现类
 * HttpClient默认跳转：httpClientBuilder.setRedirectStrategy(new LaxRedirectStrategy());
 * 上述代码在post/redirect/post这种情况下不会传递原有请求的数据信息。
 *
 * @author zido
 */
public class CustomRedirectStrategy extends LaxRedirectStrategy {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
        URI uri = getLocationURI(request, response, context);
        String method = request.getRequestLine().getMethod();
        if (Http.Method.POST.equalsIgnoreCase(method)) {
            try {
                HttpRequestWrapper httpRequestWrapper = (HttpRequestWrapper) request;
                httpRequestWrapper.setURI(uri);
                httpRequestWrapper.removeHeaders("Content-Length");
                return httpRequestWrapper;
            } catch (Exception e) {
                logger.debug("wrap error", e);
            }
            return new HttpPost(uri);
        } else {
            return new HttpGet(uri);
        }
    }
}
