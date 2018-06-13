package site.zido.elise;

import site.zido.elise.utils.StatusCode;
import site.zido.elise.utils.IdWorker;

import java.util.*;

/**
 * Website information configuration
 *
 * @author zido
 */
public class Site {

    private String domain;
    private String userAgent;
    private Map<String, String> defaultCookies = new LinkedHashMap<>();
    private Map<String, Map<String, String>> cookies = new HashMap<>();
    private Map<String, Object> extras = new HashMap<>();

    private String charset;

    private int sleepTime = 100;

    private int retryTimes = 3;

    private int cycleRetryTimes = 3;

    private int retrySleepTime = 1000;

    private int timeOut = 5000;

    private static final Set<Integer> DEFAULT_STATUS_CODE_SET = new HashSet<>();

    private Set<Integer> acceptStatCode = DEFAULT_STATUS_CODE_SET;

    private Map<String, String> headers = new HashMap<>();

    private boolean useGzip = true;

    private boolean disableCookieManagement = false;

    static {
        DEFAULT_STATUS_CODE_SET.add(StatusCode.CODE_200);
    }

    public Site addCookie(String name, String value) {
        defaultCookies.put(name, value);
        return this;
    }

    public Site setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public Map<String, String> getCookies() {
        return defaultCookies;
    }

    public Map<String, Map<String, String>> getAllCookies() {
        return cookies;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getDomain() {
        return domain;
    }

    public Site setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public Site setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public Site setTimeOut(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public Site setAcceptStatCode(Set<Integer> acceptStatCode) {
        this.acceptStatCode = acceptStatCode;
        return this;
    }

    public Set<Integer> getAcceptStatCode() {
        return acceptStatCode;
    }

    public Site setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Site addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public Site setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    public int getCycleRetryTimes() {
        return cycleRetryTimes;
    }

    public Site setCycleRetryTimes(int cycleRetryTimes) {
        this.cycleRetryTimes = cycleRetryTimes;
        return this;
    }

    public boolean isUseGzip() {
        return useGzip;
    }

    public int getRetrySleepTime() {
        return retrySleepTime;
    }

    public Site setRetrySleepTime(int retrySleepTime) {
        this.retrySleepTime = retrySleepTime;
        return this;
    }

    public Site setUseGzip(boolean useGzip) {
        this.useGzip = useGzip;
        return this;
    }

    public boolean isDisableCookieManagement() {
        return disableCookieManagement;
    }

    public Site setDisableCookieManagement(boolean disableCookieManagement) {
        this.disableCookieManagement = disableCookieManagement;
        return this;
    }

    public Task toTask() {
        return new Task() {
            @Override
            public Long getId() {
                return IdWorker.nextId();
            }

            @Override
            public Site getSite() {
                return Site.this;
            }
        };
    }

    public Site putExtra(String key, Object value) {
        this.extras.put(key, value);
        return this;
    }

    public Map<String, Object> getExtras() {
        return this.extras;
    }

    public Site setExtras(Map<String, Object> extras) {
        this.extras = extras;
        return this;
    }

    public Object getExtra(String key) {
        return this.extras.get(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Site site = (Site) o;
        return sleepTime == site.sleepTime &&
                retryTimes == site.retryTimes &&
                cycleRetryTimes == site.cycleRetryTimes &&
                retrySleepTime == site.retrySleepTime &&
                timeOut == site.timeOut &&
                useGzip == site.useGzip &&
                disableCookieManagement == site.disableCookieManagement &&
                Objects.equals(domain, site.domain) &&
                Objects.equals(userAgent, site.userAgent) &&
                Objects.equals(defaultCookies, site.defaultCookies) &&
                Objects.equals(cookies, site.cookies) &&
                Objects.equals(charset, site.charset) &&
                Objects.equals(acceptStatCode, site.acceptStatCode) &&
                Objects.equals(headers, site.headers);
    }

    @Override
    public int hashCode() {

        return Objects.hash(domain, userAgent, defaultCookies, cookies, charset, sleepTime, retryTimes, cycleRetryTimes, retrySleepTime, timeOut, acceptStatCode, headers, useGzip, disableCookieManagement);
    }
}
