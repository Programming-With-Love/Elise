package site.zido.elise;

import site.zido.elise.extractor.ModelExtractor;
import site.zido.elise.matcher.NumberExpressMatcher;
import site.zido.elise.utils.IdWorker;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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

    /**
     * judge the status code {@link site.zido.elise.matcher.NumberExpressMatcher}
     */
    private String codeAccepter = "200";

    private transient NumberExpressMatcher codeMatcher;

    private Map<String, String> headers = new HashMap<>();

    private boolean useGzip = true;

    private boolean disableCookieManagement = false;

    public Site addCookie(String name, String value) {
        defaultCookies.put(name, value);
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

    public Site setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public Site setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public Site setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public Site setTimeOut(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public Site setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public Site setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Site addHeader(String key, String value) {
        headers.put(key, value);
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

    public Site setUseGzip(boolean useGzip) {
        this.useGzip = useGzip;
        return this;
    }

    public int getRetrySleepTime() {
        return retrySleepTime;
    }

    public Site setRetrySleepTime(int retrySleepTime) {
        this.retrySleepTime = retrySleepTime;
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

            @Override
            public ModelExtractor modelExtractor() {
                return null;
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

    public String getCodeAccepter() {
        return codeAccepter;
    }

    public Site setCodeAccepter(String codeAccepter) {
        codeMatcher = new NumberExpressMatcher(codeAccepter);
        this.codeAccepter = codeAccepter;
        return this;
    }

    public synchronized NumberExpressMatcher getCodeMatcher() {
        if (this.codeMatcher == null) {
            this.codeMatcher = new NumberExpressMatcher(codeAccepter);
        }
        return this.codeMatcher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Site site = (Site) o;

        if (sleepTime != site.sleepTime) return false;
        if (retryTimes != site.retryTimes) return false;
        if (cycleRetryTimes != site.cycleRetryTimes) return false;
        if (retrySleepTime != site.retrySleepTime) return false;
        if (timeOut != site.timeOut) return false;
        if (useGzip != site.useGzip) return false;
        if (disableCookieManagement != site.disableCookieManagement) return false;
        if (domain != null ? !domain.equals(site.domain) : site.domain != null) return false;
        if (userAgent != null ? !userAgent.equals(site.userAgent) : site.userAgent != null) return false;
        if (defaultCookies != null ? !defaultCookies.equals(site.defaultCookies) : site.defaultCookies != null)
            return false;
        if (cookies != null ? !cookies.equals(site.cookies) : site.cookies != null) return false;
        if (extras != null ? !extras.equals(site.extras) : site.extras != null) return false;
        if (charset != null ? !charset.equals(site.charset) : site.charset != null) return false;
        if (codeAccepter != null ? !codeAccepter.equals(site.codeAccepter) : site.codeAccepter != null) return false;
        return headers != null ? headers.equals(site.headers) : site.headers == null;
    }

    @Override
    public int hashCode() {
        int result = domain != null ? domain.hashCode() : 0;
        result = 31 * result + (userAgent != null ? userAgent.hashCode() : 0);
        result = 31 * result + (defaultCookies != null ? defaultCookies.hashCode() : 0);
        result = 31 * result + (cookies != null ? cookies.hashCode() : 0);
        result = 31 * result + (extras != null ? extras.hashCode() : 0);
        result = 31 * result + (charset != null ? charset.hashCode() : 0);
        result = 31 * result + sleepTime;
        result = 31 * result + retryTimes;
        result = 31 * result + cycleRetryTimes;
        result = 31 * result + retrySleepTime;
        result = 31 * result + timeOut;
        result = 31 * result + (codeAccepter != null ? codeAccepter.hashCode() : 0);
        result = 31 * result + (headers != null ? headers.hashCode() : 0);
        result = 31 * result + (useGzip ? 1 : 0);
        result = 31 * result + (disableCookieManagement ? 1 : 0);
        return result;
    }
}
