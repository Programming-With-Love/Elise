package site.zido.elise;

import site.zido.elise.select.configurable.ConfigurableModelExtractor;
import site.zido.elise.select.configurable.DefRootExtractor;
import site.zido.elise.select.configurable.ModelExtractor;

/**
 * default extractor task
 * <br>
 *
 * @author zido
 */
public class DefaultTask implements Task {
    private Long id;
    private Site site;
    private DefRootExtractor defExtractor;

    /**
     * Instantiates a new Default task.
     */
    public DefaultTask() {

    }

    /**
     * Instantiates a new Default task.
     *
     * @param id        the id
     * @param site      the site
     * @param extractor the extractor
     */
    public DefaultTask(Long id, Site site, DefRootExtractor extractor) {
        this.id = id;
        this.site = site;
        this.defExtractor = extractor;
    }

    @Override
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     * @return the id
     */
    public DefaultTask setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public Site getSite() {
        return site;
    }

    /**
     * Sets site.
     *
     * @param site the site
     * @return the site
     */
    public DefaultTask setSite(Site site) {
        this.site = site;
        return this;
    }

    @Override
    public ModelExtractor modelExtractor() {
        return new ConfigurableModelExtractor(defExtractor);
    }

    /**
     * Gets def extractor.
     *
     * @return the def extractor
     */
    public DefRootExtractor getDefExtractor() {
        return defExtractor;
    }

    /**
     * Sets def extractor.
     *
     * @param defExtractor the def extractor
     * @return the def extractor
     */
    public DefaultTask setDefExtractor(DefRootExtractor defExtractor) {
        this.defExtractor = defExtractor;
        return this;
    }
}
