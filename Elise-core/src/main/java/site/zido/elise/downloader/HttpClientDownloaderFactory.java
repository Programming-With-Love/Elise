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
import site.zido.elise.Task;
import site.zido.elise.custom.Config;
import site.zido.elise.custom.GlobalConfig;
import site.zido.elise.downloader.httpclient.CustomRedirectStrategy;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpClientDownloaderFactory implements DownloaderFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientDownloaderFactory.class);
    private static final String ACCEPT_ENCODING = "Accept-Encoding";
    private static final String GZIP = "gzip";
    private PoolingHttpClientConnectionManager connectionManager;
    private Map<Long, Downloader> downloaderContainer = new ConcurrentHashMap<>();

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
        return downloaderContainer.computeIfAbsent(task.getId(), key -> {
            Config config = task.modelExtractor().getConfig();
            HttpClientBuilder builder = HttpClients.custom();
            builder.setConnectionManager(connectionManager);
            String userAgent = config.get(GlobalConfig.KEY_USER_AGENT);
            builder.setUserAgent(userAgent);
            Boolean useGzip = config.get(GlobalConfig.KEY_USE_GZIP);
            if (useGzip != null && useGzip) {
                builder.addInterceptorFirst((HttpRequestInterceptor) (request, context) -> {
                    if (!request.containsHeader(ACCEPT_ENCODING)) {
                        request.addHeader(ACCEPT_ENCODING, GZIP);
                    }
                });
            }
            builder.setRedirectStrategy(new CustomRedirectStrategy());

            SocketConfig.Builder socketConfigBuilder = SocketConfig.custom();
            socketConfigBuilder.setSoKeepAlive(true).setTcpNoDelay(true);
            int timeout = config.get(GlobalConfig.KEY_TIME_OUT);
            socketConfigBuilder.setSoTimeout(timeout);
            SocketConfig socketConfig = socketConfigBuilder.build();
            builder.setDefaultSocketConfig(socketConfig);
            connectionManager.setDefaultSocketConfig(socketConfig);
            int retryTimes = config.get(GlobalConfig.KEY_RETRY_TIMES);
            builder.setRetryHandler(new StandardHttpRequestRetryHandler(retryTimes, true));

            boolean disableCookie = config.get(GlobalConfig.KEY_DISABLE_COOKIE);
            if (disableCookie) {
                builder.disableCookieManagement();
            } else {
                CookieStore store = new BasicCookieStore();
                Map<String, String> cookies = config.get(GlobalConfig.KEY_COOKIE);
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
