package site.zido.elise.custom;

public class GlobalConfig extends MappedConfig {
    private static final long serialVersionUID = -6234664119002484979L;
    public static final String KEY_USER_AGENT = "userAgent";
    public static final String KEY_COOKIE = "cookie";
    public static final String KEY_CHARSET = "charset";
    public static final String KEY_SLEEP_TIME = "sleepTime";
    public static final String KEY_RETRY_TIMES = "retryTimes";
    public static final String KEY_OUT_TIME = "outTime";
    public static final String KEY_DOWNLOAD_MODE = "downloadMode";
    public static final String KEY_SUCCESS_CODE = "successCode";
    public static final String KEY_DISABLE_COOKIE = "disableCookie";
    public static final String KEY_HEADERS = "headers";
    public static final String KEY_PROXY = "proxy";

    public void setUserAgent(String userAgent) {
        put(KEY_USER_AGENT, userAgent);
    }

    public void setCookie(String cookie) {
        put(KEY_COOKIE, cookie);
    }

    public void setCharset(String charset) {
        put(KEY_CHARSET, charset);
    }

    public void setSleepTime(String sleepTime) {
        put(KEY_SLEEP_TIME, sleepTime);
    }

    public void setRetryTimes(String retryTimes) {
        put(KEY_RETRY_TIMES, retryTimes);
    }

    public void setOutTime(String outTime) {
        put(KEY_OUT_TIME, outTime);
    }

    public void setDownloadMode(String downloadMode) {
        put(KEY_DOWNLOAD_MODE, downloadMode);
    }

    public void setSuccessCode(String successCode) {
        put(KEY_SUCCESS_CODE, successCode);
    }

    public void setDisableCookie(String disableCookie) {
        put(KEY_DISABLE_COOKIE, disableCookie);
    }

    public void setHeaders(String headers) {
        put(KEY_HEADERS, headers);
    }

    public void setProxy(String proxy) {
        put(KEY_PROXY, proxy);
    }
}
