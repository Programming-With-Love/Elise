package site.zido.elise.downloader;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.CookieStore;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.custom.GlobalConfig;
import site.zido.elise.downloader.httpclient.CustomRedirectStrategy;
import site.zido.elise.task.Task;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Http client downloader factory.
 *
 * @author zido
 */
public class HttpClientDownloaderFactory implements DownloaderFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientDownloaderFactory.class);
    private static final String ACCEPT_ENCODING = "Accept-Encoding";
    private static final String GZIP = "gzip";
    private PoolingHttpClientConnectionManager connectionManager;
    private Map<Long, Downloader> downloaderContainer = new ConcurrentHashMap<>();

    /**
     * Instantiates a new Http client downloader factory.
     */
    public HttpClientDownloaderFactory() {
        Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", buildSSLConnectionSocketFactory())
                .build();
        connectionManager = new PoolingHttpClientConnectionManager(reg);
        connectionManager.setDefaultMaxPerRoute(100);
    }

    private SSLConnectionSocketFactory buildSSLConnectionSocketFactory() {
        try {
            // 优先绕过安全证书
            return new SSLConnectionSocketFactory(createIgnoreVerifySSL(), new String[]{"SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"},
                    null,
                    new DefaultHostnameVerifier());
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            LOGGER.error("create ssl connection socket factory error,and use default ssl connection", e);
        }
        return SSLConnectionSocketFactory.getSocketFactory();
    }

    private SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        X509TrustManager trustManager = new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        };

        SSLContext sc = SSLContext.getInstance("SSLv3");
        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

    @Override
    public Downloader create(Task task) {
        //The same task is only created once
        return downloaderContainer.computeIfAbsent(task.getId(), key -> {
            HttpClientBuilder builder = HttpClients.custom();
            //all client use the same connection pool
            builder.setConnectionManager(connectionManager);
            GlobalConfig config = new GlobalConfig(task.modelExtractor().getConfig());
            String userAgent = config.getUserAgent();
            builder.setUserAgent(userAgent);
            boolean useGzip = config.getUseGzip();
            if (useGzip) {
                builder.addInterceptorFirst((HttpRequestInterceptor) (request, context) -> {
                    if (!request.containsHeader(ACCEPT_ENCODING)) {
                        request.addHeader(ACCEPT_ENCODING, GZIP);
                    }
                });
            }
            //All requests can respond to the response code 302 by the same way
            builder.setRedirectStrategy(new CustomRedirectStrategy());

            SocketConfig.Builder socketConfigBuilder = SocketConfig.custom();
            socketConfigBuilder.setSoKeepAlive(true).setTcpNoDelay(true);
            int timeout = config.getTimeout();
            socketConfigBuilder.setSoTimeout(timeout);
            SocketConfig socketConfig = socketConfigBuilder.build();
            builder.setDefaultSocketConfig(socketConfig);
            connectionManager.setDefaultSocketConfig(socketConfig);
            int retryTimes = config.getRetryTimes();
            builder.setRetryHandler(new StandardHttpRequestRetryHandler(retryTimes, true));
            boolean disableCookie = config.get(GlobalConfig.KEY_DISABLE_COOKIE);
            if (disableCookie) {
                builder.disableCookieManagement();
            } else {
                CookieStore store = new BasicCookieStore();
                Map<String, String> cookies = config.getCookies();
                for (Map.Entry<String, String> entry : cookies.entrySet()) {
                    BasicClientCookie cookie = new BasicClientCookie(entry.getKey(), entry.getValue());
                    store.addCookie(cookie);
                }
                builder.setDefaultCookieStore(store);
            }
            return new HttpClientDownloader(builder.build());
        });
    }
}
