package site.zido.elise.custom;

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
     * The constant KEY_RETRY_TIMES.
     */
    public static final String KEY_RETRY_TIMES = "retryTimes";
    /**
     * The constant KEY_OUT_TIME.
     */
    public static final String KEY_OUT_TIME = "outTime";
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
     * The constant KEY_PROXY.
     */
    public static final String KEY_PROXY = "proxy";
    private static final long serialVersionUID = -6234664119002484979L;

    /**
     * Sets user agent.
     *
     * @param userAgent the user agent
     */
    public void setUserAgent(String userAgent) {
        put(KEY_USER_AGENT, userAgent);
    }

    /**
     * Sets cookie.
     *
     * @param cookie the cookie
     */
    public void setCookie(String cookie) {
        put(KEY_COOKIE, cookie);
    }

    /**
     * Sets charset.
     *
     * @param charset the charset
     */
    public void setCharset(String charset) {
        put(KEY_CHARSET, charset);
    }

    /**
     * Sets sleep time.
     *
     * @param sleepTime the sleep time
     */
    public void setSleepTime(String sleepTime) {
        put(KEY_SLEEP_TIME, sleepTime);
    }

    /**
     * Sets retry times.
     *
     * @param retryTimes the retry times
     */
    public void setRetryTimes(String retryTimes) {
        put(KEY_RETRY_TIMES, retryTimes);
    }

    /**
     * Sets out time.
     *
     * @param outTime the out time
     */
    public void setOutTime(String outTime) {
        put(KEY_OUT_TIME, outTime);
    }

    /**
     * Sets download mode.
     *
     * @param downloadMode the download mode
     */
    public void setDownloadMode(String downloadMode) {
        put(KEY_DOWNLOAD_MODE, downloadMode);
    }

    /**
     * Sets success code.
     *
     * @param successCode the success code
     */
    public void setSuccessCode(String successCode) {
        put(KEY_SUCCESS_CODE, successCode);
    }

    /**
     * Sets disable cookie.
     *
     * @param disableCookie the disable cookie
     */
    public void setDisableCookie(String disableCookie) {
        put(KEY_DISABLE_COOKIE, disableCookie);
    }

    /**
     * Sets headers.
     *
     * @param headers the headers
     */
    public void setHeaders(String headers) {
        put(KEY_HEADERS, headers);
    }

    /**
     * Sets proxy.
     *
     * @param proxy the proxy
     */
    public void setProxy(String proxy) {
        put(KEY_PROXY, proxy);
    }
}
