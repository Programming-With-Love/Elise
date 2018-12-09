package site.zido.elise.custom;

import site.zido.elise.http.Header;
import site.zido.elise.proxy.Proxy;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The type Global config.
 *
 * @author zido
 */
public class GlobalConfig extends MappedConfig {
    /**
     * The constant KEY_USER_AGENT.
     */
    public static final String KEY_USER_AGENT = "userAgent";
    /**
     * The constant KEY_COOKIE.
     */
    public static final String KEY_COOKIE = "cookie";
    /**
     * The constant KEY_CHARSET.
     */
    public static final String KEY_CHARSET = "charset";
    /**
     * The constant KEY_SLEEP_TIME.
     */
    public static final String KEY_SLEEP_TIME = "sleepTime";
    /**
     * The number of times the request was retried when the download failed
     */
    public static final String KEY_RETRY_TIMES = "retryTimes";
    /**
     * The constant KEY_TIME_OUT.
     */
    public static final String KEY_TIME_OUT = "outTime";
    /**
     * The constant KEY_DOWNLOAD_MODE.
     */
    public static final String KEY_DOWNLOAD_MODE = "downloadMode";
    /**
     * The constant KEY_SUCCESS_CODE.
     */
    public static final String KEY_SUCCESS_CODE = "successCode";
    /**
     * The constant KEY_DISABLE_COOKIE.
     */
    public static final String KEY_DISABLE_COOKIE = "disableCookie";
    /**
     * The constant KEY_HEADERS.
     */
    public static final String KEY_HEADERS = "headers";
    /**
     * The number of retries that were added to the task scheduler when the download failed
     */
    public static final String KEY_SCHEDULE_RETRY_TIMES = "scheduleRetryTimes";
    /**
     * The constant KEY_POOL_SIZE.
     */
    public static final String KEY_POOL_SIZE = "poolSize";
    /**
     * The constant KEY_USE_GZIP.
     */
    public static final String KEY_USE_GZIP = "useGzip";
    /**
     * The constant KEY_PROXY.
     */
    public static final String KEY_PROXY = "proxy";
    private static final List<Header> EMPTY_HEADERS = new LinkedList<>();
    private static final long serialVersionUID = -6234664119002484979L;

    /**
     * Instantiates a new Global config.
     */
    public GlobalConfig() {
    }

    /**
     * Instantiates a new Global config.
     *
     * @param config the config
     */
    public GlobalConfig(Map<String, Object> config) {
        super(config);
    }

    /**
     * Gets user agent.
     *
     * @return the user agent
     */
    public String getUserAgent() {
        return get(KEY_USER_AGENT);
    }

    /**
     * Gets cookies.
     *
     * @return the cookies
     */
    public Map<String, String> getCookies() {
        return get(KEY_COOKIE);
    }

    /**
     * Gets charset.
     *
     * @return the charset
     */
    public String getCharset() {
        return get(KEY_CHARSET);
    }

    /**
     * Gets disable cookie.
     *
     * @return the disable cookie
     */
    public boolean getDisableCookie() {
        return (boolean) get(KEY_DISABLE_COOKIE);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Map)) {
            return false;
        }
        Object otherValue;
        for (String s : this.keySet()) {
            otherValue = ((Map) o).get(s);
            if (!Objects.equals(get(s), otherValue)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets headers.
     *
     * @return the headers
     */
    public List<Header> getHeaders() {
        return get(KEY_HEADERS);
    }

    /**
     * Gets use gzip.
     *
     * @return the use gzip
     */
    public boolean getUseGzip() {
        return (boolean) get(KEY_USE_GZIP);
    }

    /**
     * Gets timeout.
     *
     * @return the timeout
     */
    public int getTimeout() {
        return (int) get(KEY_TIME_OUT);
    }

    /**
     * Gets retry times.
     *
     * @return the retry times
     */
    public int getRetryTimes() {
        return (int) get(KEY_RETRY_TIMES);
    }

    /**
     * Gets proxy.
     *
     * @return the proxy
     */
    public Proxy getProxy() {
        return get(KEY_PROXY);
    }

}
