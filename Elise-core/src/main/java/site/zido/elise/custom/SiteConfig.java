package site.zido.elise.custom;

/**
 * The type Site config.
 *
 * @author zido
 */
public class SiteConfig extends GlobalConfig {
    /**
     * The constant KEY_SITE.
     */
    public static final String KEY_SITE = "site";
    private static final long serialVersionUID = 7284323147466259820L;

    /**
     * Sets site.
     *
     * @param site the site
     */
    public void setSite(String site) {
        put(KEY_SITE, site);
    }
}
