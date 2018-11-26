package site.zido.elise.http.impl;

import site.zido.elise.http.Cookie;

import java.util.Date;

/**
 * The type Default cookie.
 *
 * @author zido
 */
public class DefaultCookie extends DefaultHeader implements Cookie {
    private static final long serialVersionUID = -492077641315574434L;
    private String domain;
    private String path;
    private Date expiryDate;
    private boolean secure;

    /**
     * Instantiates a new Default cookie.
     *
     * @param name  the name
     * @param value the value
     */
    public DefaultCookie(String name, String value) {
        super(name, value);
    }

    /**
     * Instantiates a new Default cookie.
     *
     * @param name  the name
     * @param value the value
     * @param path  the path
     */
    public DefaultCookie(String name, String value, String path) {
        super(name, value);
        this.path = path;
    }

    /**
     * Instantiates a new Default cookie.
     *
     * @param name       the name
     * @param value      the value
     * @param domain     the domain
     * @param path       the path
     * @param expiryDate the expiry date
     * @param secure     the secure
     */
    public DefaultCookie(String name, String value, String domain, String path, Date expiryDate, boolean secure) {
        super(name, value);
        this.domain = domain;
        this.path = path;
        this.expiryDate = expiryDate;
        this.secure = secure;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Date getExpiryDate() {
        return expiryDate;
    }

    @Override
    public boolean isSecure() {
        return secure;
    }

    @Override
    public String getDomain() {
        return domain;
    }
}
