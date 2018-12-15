package site.zido.elise.custom;

import site.zido.elise.http.Cookie;
import site.zido.elise.http.Header;
import site.zido.elise.http.impl.DefaultCookie;
import site.zido.elise.select.NumberMatcherSelectHandler;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static site.zido.elise.custom.GlobalConfig.*;

/**
 * The type Global config builder.
 *
 * @author zido
 */
public class GlobalConfigBuilder extends MappedConfig {

    private static final long serialVersionUID = 6226554722300087924L;

    /**
     * Instantiates a new Global config builder.
     */
    protected GlobalConfigBuilder() {
    }

    /**
     * Create global config builder.
     *
     * @return the global config builder
     */
    public static GlobalConfigBuilder create() {
        return new GlobalConfigBuilder();
    }

    /**
     * Defaults global config.
     *
     * @return the global config
     */
    public static GlobalConfig defaults() {
        return create().build();
    }

    /**
     * Sets user agent.
     *
     * @param userAgent the user agent
     * @return the user agent
     */
    public GlobalConfigBuilder setUserAgent(String userAgent) {
        put(KEY_USER_AGENT, userAgent);
        return this;
    }

    /**
     * Sets cookie.
     *
     * @param cookie the cookie
     * @return the cookie
     */
    public GlobalConfigBuilder setCookie(List<Cookie> cookie) {
        put(KEY_COOKIE, cookie);
        return this;
    }

    /**
     * Add cookie global config builder.
     *
     * @param cookies the cookies
     * @return the global config builder
     */
    public GlobalConfigBuilder addCookie(Cookie... cookies) {
        return addHeaderByKey(KEY_COOKIE, cookies);
    }

    /**
     * Add cookie global config builder.
     *
     * @param name  the name
     * @param value the value
     * @return the global config builder
     */
    public GlobalConfigBuilder addCookie(String name, String value) {
        return addCookie(new DefaultCookie(name, value));
    }

    /**
     * Sets charset.
     *
     * @param charset the charset
     * @return the charset
     */
    public GlobalConfigBuilder setCharset(String charset) {
        if (!Charset.isSupported(charset)) {
            throw new IllegalCharsetNameException(charset);
        }
        put(KEY_CHARSET, charset);
        return this;
    }

    /**
     * Sets charset.
     *
     * @param charset the charset
     * @return the charset
     */
    public GlobalConfigBuilder setCharset(Charset charset) {
        put(KEY_CHARSET, charset.name());
        return this;
    }

    /**
     * Sets sleep time.
     *
     * @param sleepTime the sleep time
     * @return the sleep time
     */
    public GlobalConfigBuilder setSleepTime(int sleepTime) {
        put(KEY_SLEEP_TIME, sleepTime);
        return this;
    }

    /**
     * Sets out time.
     *
     * @param timeout the out time
     * @return the timeout
     */
    public GlobalConfigBuilder setTimeout(int timeout) {
        put(KEY_TIME_OUT, timeout);
        return this;
    }

    /**
     * Sets download mode.
     *
     * @param downloadMode the download mode
     * @return the download mode
     */
    public GlobalConfigBuilder setDownloadMode(String downloadMode) {
        put(KEY_DOWNLOAD_MODE, downloadMode);
        return this;
    }

    /**
     * Sets success code.
     *
     * @param codeExpress the success code
     * @return the success code
     */
    public GlobalConfigBuilder setSuccessCode(String codeExpress) {
        if (!NumberMatcherSelectHandler.isSupport(codeExpress)) {
            throw new IllegalArgumentException("code match express:" + codeExpress + " no support");
        }
        put(KEY_SUCCESS_CODE, codeExpress);
        return this;
    }

    /**
     * Sets disable cookie.
     *
     * @param disableCookie the disable cookie
     * @return the disable cookie
     */
    public GlobalConfigBuilder setDisableCookie(boolean disableCookie) {
        put(KEY_DISABLE_COOKIE, disableCookie);
        return this;
    }

    /**
     * Sets pool size.
     *
     * @param poolSize the pool size
     * @return the pool size
     */
    public GlobalConfigBuilder setPoolSize(int poolSize) {
        put(KEY_POOL_SIZE, poolSize);
        return this;
    }

    /**
     * Sets headers.
     *
     * @param headers the headers
     * @return the headers
     */
    public GlobalConfigBuilder setHeaders(List<Header> headers) {
        put(KEY_HEADERS, headers);
        return this;
    }

    /**
     * Add header global config builder.
     *
     * @param header the header
     * @return the global config builder
     */
    public GlobalConfigBuilder addHeader(Header... header) {
        return addHeaderByKey(KEY_HEADERS, header);
    }

    private GlobalConfigBuilder addHeaderByKey(String key, Header[] header) {
        List<Header> headers = get(key);
        if (headers == null) {
            headers = new ArrayList<>();
            put(key, headers);
        }
        headers.addAll(Arrays.asList(header));
        return this;
    }

    /**
     * Sets retry times.
     *
     * @param retryTimes the retry times
     * @return the retry times
     */
    public GlobalConfigBuilder setRetryTimes(int retryTimes) {
        put(KEY_RETRY_TIMES, retryTimes);
        return this;
    }

    /**
     * Build global config.
     *
     * @return the global config
     */
    public GlobalConfig build() {
        final GlobalConfig config = new GlobalConfig(this);
        if (!config.containsKey(KEY_USER_AGENT)) {
            config.put(KEY_USER_AGENT, "Mozilla/5.0");
        }
        if (!config.containsKey(KEY_SLEEP_TIME)) {
            config.put(KEY_SLEEP_TIME, 0);
        }
        if (!config.containsKey(KEY_RETRY_TIMES)) {
            config.put(KEY_RETRY_TIMES, 3);
        }
        if (!config.containsKey(KEY_TIME_OUT)) {
            config.put(KEY_TIME_OUT, 5000);
        }
        if (!config.containsKey(KEY_DOWNLOAD_MODE)) {
            config.put(KEY_DOWNLOAD_MODE, "httpclient");
        }
        if (!config.containsKey(KEY_SUCCESS_CODE)) {
            config.put(KEY_SUCCESS_CODE, "200");
        }
        if (!config.containsKey(KEY_DISABLE_COOKIE)) {
            config.put(KEY_DISABLE_COOKIE, false);
        }
        if (!config.containsKey(KEY_SCHEDULE_RETRY_TIMES)) {
            config.put(KEY_SCHEDULE_RETRY_TIMES, 3);
        }
        if (!config.containsKey(KEY_POOL_SIZE)) {
            config.put(KEY_POOL_SIZE, 500);
        }
        if (!config.containsKey(KEY_USE_GZIP)) {
            config.put(KEY_USE_GZIP, false);
        }

        if (!config.containsKey(KEY_CHARSET)) {
            config.put(KEY_CHARSET, "utf-8");
        }
        return config;
    }

}
