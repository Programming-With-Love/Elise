package site.zido.elise.custom;

import site.zido.elise.http.Cookie;
import site.zido.elise.http.Header;
import site.zido.elise.http.impl.DefaultCookie;
import site.zido.elise.select.matcher.NumberExpressMatcher;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static site.zido.elise.custom.GlobalConfig.*;

public class GlobalConfigBuilder {
    private MappedConfig config = new MappedConfig();

    protected GlobalConfigBuilder() {
    }

    public static GlobalConfigBuilder create() {
        return new GlobalConfigBuilder();
    }

    /**
     * Sets user agent.
     *
     * @param userAgent the user agent
     */
    public GlobalConfigBuilder setUserAgent(String userAgent) {
        config.put(KEY_USER_AGENT, userAgent);
        return this;
    }

    /**
     * Sets cookie.
     *
     * @param cookie the cookie
     */
    public GlobalConfigBuilder setCookie(List<Cookie> cookie) {
        config.put(KEY_COOKIE, cookie);
        return this;
    }

    public GlobalConfigBuilder addCookie(Cookie... cookies) {
        return addHeaderByKey(KEY_COOKIE, cookies);
    }

    public GlobalConfigBuilder addCookie(String name, String value) {
        return addCookie(new DefaultCookie(name, value));
    }

    /**
     * Sets charset.
     *
     * @param charset the charset
     */
    public GlobalConfigBuilder setCharset(String charset) {
        if (!Charset.isSupported(charset)) {
            throw new IllegalCharsetNameException(charset);
        }
        config.put(KEY_CHARSET, charset);
        return this;
    }

    public GlobalConfigBuilder setCharset(Charset charset) {
        config.put(KEY_CHARSET, charset.name());
        return this;
    }

    /**
     * Sets sleep time.
     *
     * @param sleepTime the sleep time
     */
    public GlobalConfigBuilder setSleepTime(int sleepTime) {
        config.put(KEY_SLEEP_TIME, sleepTime);
        return this;
    }

    /**
     * Sets out time.
     *
     * @param timeout the out time
     */
    public GlobalConfigBuilder setTimeout(int timeout) {
        config.put(KEY_TIME_OUT, timeout);
        return this;
    }

    /**
     * Sets download mode.
     *
     * @param downloadMode the download mode
     */
    public GlobalConfigBuilder setDownloadMode(String downloadMode) {
        config.put(KEY_DOWNLOAD_MODE, downloadMode);
        return this;
    }

    /**
     * Sets success code.
     *
     * @param codeExpress the success code
     */
    public GlobalConfigBuilder setSuccessCode(String codeExpress) {
        if (!NumberExpressMatcher.isSupport(codeExpress)) {
            throw new IllegalArgumentException("code match express:" + codeExpress + " no support");
        }
        config.put(KEY_SUCCESS_CODE, codeExpress);
        return this;
    }

    /**
     * Sets disable cookie.
     *
     * @param disableCookie the disable cookie
     */
    public GlobalConfigBuilder setDisableCookie(boolean disableCookie) {
        config.put(KEY_DISABLE_COOKIE, disableCookie);
        return this;
    }

    public GlobalConfigBuilder setPoolSize(int poolSize) {
        config.put(KEY_POOL_SIZE, poolSize);
        return this;
    }

    /**
     * Sets headers.
     *
     * @param headers the headers
     */
    public GlobalConfigBuilder setHeaders(List<Header> headers) {
        config.put(KEY_HEADERS, headers);
        return this;
    }

    public GlobalConfigBuilder addHeader(Header... header) {
        return addHeaderByKey(KEY_HEADERS, header);
    }

    private GlobalConfigBuilder addHeaderByKey(String key, Header[] header) {
        List<Header> headers = config.get(key);
        if (headers == null) {
            headers = new ArrayList<>();
            config.put(key, headers);
        }
        headers.addAll(Arrays.asList(header));
        return this;
    }

    /**
     * Sets retry times.
     *
     * @param retryTimes the retry times
     */
    public GlobalConfigBuilder setRetryTimes(int retryTimes) {
        config.put(KEY_RETRY_TIMES, retryTimes);
        return this;
    }

    public GlobalConfig build() {
        return new GlobalConfig(config);
    }

    public static GlobalConfig defaults() {
        return create().setCharset("utf-8").build();
    }

}
