package site.zido.elise;

import site.zido.elise.custom.SiteConfig;
import site.zido.elise.select.configurable.ModelExtractor;
import site.zido.elise.select.matcher.CompilerException;
import site.zido.elise.select.matcher.NumberExpressMatcher;
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
     * judge the status code {@link NumberExpressMatcher}
     */
    private String codeAccepter = "200";

    private transient NumberExpressMatcher codeMatcher;

    private Map<String, String> headers = new HashMap<>();

    private boolean useGzip = true;

    private boolean disableCookieManagement = false;

    /**
     * Add cookie site.
     *
     * @param name  the name
     * @param value the value
     * @return the site
     */
    public Site addCookie(String name, String value) {
        defaultCookies.put(name, value);
        return this;
    }

    /**
     * Gets cookies.
     *
     * @return the cookies
     */
    public Map<String, String> getCookies() {
        return defaultCookies;
    }

    /**
     * Gets all cookies.
     *
     * @return the all cookies
     */
    public Map<String, Map<String, String>> getAllCookies() {
        return cookies;
    }

    /**
     * Gets user agent.
     *
     * @return the user agent
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * Sets user agent.
     *
     * @param userAgent the user agent
     * @return the user agent
     */
    public Site setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    /**
     * Gets domain.
     *
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Sets domain.
     *
     * @param domain the domain
     * @return the domain
     */
    public Site setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    /**
     * Gets charset.
     *
     * @return the charset
     */
    public String getCharset() {
        return charset;
    }

    /**
     * Sets charset.
     *
     * @param charset the charset
     * @return the charset
     */
    public Site setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     * Gets time out.
     *
     * @return the time out
     */
    public int getTimeOut() {
        return timeOut;
    }

    /**
     * Sets time out.
     *
     * @param timeOut the time out
     * @return the time out
     */
    public Site setTimeOut(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    /**
     * Gets sleep time.
     *
     * @return the sleep time
     */
    public int getSleepTime() {
        return sleepTime;
    }

    /**
     * Sets sleep time.
     *
     * @param sleepTime the sleep time
     * @return the sleep time
     */
    public Site setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    /**
     * Gets retry times.
     *
     * @return the retry times
     */
    public int getRetryTimes() {
        return retryTimes;
    }

    /**
     * Sets retry times.
     *
     * @param retryTimes the retry times
     * @return the retry times
     */
    public Site setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    /**
     * Gets headers.
     *
     * @return the headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Add header site.
     *
     * @param key   the key
     * @param value the value
     * @return the site
     */
    public Site addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    /**
     * Gets cycle retry times.
     *
     * @return the cycle retry times
     */
    public int getCycleRetryTimes() {
        return cycleRetryTimes;
    }

    /**
     * Sets cycle retry times.
     *
     * @param cycleRetryTimes the cycle retry times
     * @return the cycle retry times
     */
    public Site setCycleRetryTimes(int cycleRetryTimes) {
        this.cycleRetryTimes = cycleRetryTimes;
        return this;
    }

    /**
     * Is use gzip boolean.
     *
     * @return the boolean
     */
    public boolean isUseGzip() {
        return useGzip;
    }

    /**
     * Sets use gzip.
     *
     * @param useGzip the use gzip
     * @return the use gzip
     */
    public Site setUseGzip(boolean useGzip) {
        this.useGzip = useGzip;
        return this;
    }

    /**
     * Gets retry sleep time.
     *
     * @return the retry sleep time
     */
    public int getRetrySleepTime() {
        return retrySleepTime;
    }

    /**
     * Sets retry sleep time.
     *
     * @param retrySleepTime the retry sleep time
     * @return the retry sleep time
     */
    public Site setRetrySleepTime(int retrySleepTime) {
        this.retrySleepTime = retrySleepTime;
        return this;
    }

    /**
     * Is disable cookie management boolean.
     *
     * @return the boolean
     */
    public boolean isDisableCookieManagement() {
        return disableCookieManagement;
    }

    /**
     * Sets disable cookie management.
     *
     * @param disableCookieManagement the disable cookie management
     * @return the disable cookie management
     */
    public Site setDisableCookieManagement(boolean disableCookieManagement) {
        this.disableCookieManagement = disableCookieManagement;
        return this;
    }

    /**
     * To task task.
     *
     * @return the task
     */
    public Task toTask() {
        return new Task() {
            @Override
            public Long getId() {
                return IdWorker.nextId();
            }

            @Override
            public SiteConfig getSite() {
                return Site.this;
            }

            @Override
            public ModelExtractor modelExtractor() {
                return null;
            }
        };
    }

    /**
     * Put extra site.
     *
     * @param key   the key
     * @param value the value
     * @return the site
     */
    public Site putExtra(String key, Object value) {
        this.extras.put(key, value);
        return this;
    }

    /**
     * Gets extras.
     *
     * @return the extras
     */
    public Map<String, Object> getExtras() {
        return this.extras;
    }

    /**
     * Sets extras.
     *
     * @param extras the extras
     * @return the extras
     */
    public Site setExtras(Map<String, Object> extras) {
        this.extras = extras;
        return this;
    }

    /**
     * Gets extra.
     *
     * @param key the key
     * @return the extra
     */
    public Object getExtra(String key) {
        return this.extras.get(key);
    }

    /**
     * Gets code accepter.
     *
     * @return the code accepter
     */
    public String getCodeAccepter() {
        return codeAccepter;
    }

    /**
     * Sets code accepter.
     *
     * @param codeAccepter the code accepter
     * @return the code accepter
     */
    public Site setCodeAccepter(String codeAccepter) {
        try {
            codeMatcher = new NumberExpressMatcher(codeAccepter);
        } catch (CompilerException e) {
            e.printStackTrace();
        }
        this.codeAccepter = codeAccepter;
        return this;
    }

    /**
     * Gets code matcher.
     *
     * @return the code matcher
     */
    public synchronized NumberExpressMatcher getCodeMatcher() {
        if (this.codeMatcher == null) {
            try {
                this.codeMatcher = new NumberExpressMatcher(codeAccepter);
            } catch (CompilerException e) {
                e.printStackTrace();
            }
        }
        return this.codeMatcher;
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

        if (sleepTime != site.sleepTime) {
            return false;
        }
        if (retryTimes != site.retryTimes) {
            return false;
        }
        if (cycleRetryTimes != site.cycleRetryTimes) {
            return false;
        }
        if (retrySleepTime != site.retrySleepTime) {
            return false;
        }
        if (timeOut != site.timeOut) {
            return false;
        }
        if (useGzip != site.useGzip) {
            return false;
        }
        if (disableCookieManagement != site.disableCookieManagement) {
            return false;
        }
        if (domain != null ? !domain.equals(site.domain) : site.domain != null) {
            return false;
        }
        if (userAgent != null ? !userAgent.equals(site.userAgent) : site.userAgent != null) {
            return false;
        }
        if (defaultCookies != null ? !defaultCookies.equals(site.defaultCookies) : site.defaultCookies != null) {
            return false;
        }
        if (cookies != null ? !cookies.equals(site.cookies) : site.cookies != null) {
            return false;
        }
        if (extras != null ? !extras.equals(site.extras) : site.extras != null) {
            return false;
        }
        if (charset != null ? !charset.equals(site.charset) : site.charset != null) {
            return false;
        }
        if (codeAccepter != null ? !codeAccepter.equals(site.codeAccepter) : site.codeAccepter != null) {
            return false;
        }
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
