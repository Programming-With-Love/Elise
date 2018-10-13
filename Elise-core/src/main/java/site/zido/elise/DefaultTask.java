package site.zido.elise;

import site.zido.elise.configurable.ConfigurableModelExtractor;
import site.zido.elise.configurable.DefRootExtractor;
import site.zido.elise.extractor.ModelExtractor;

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

    public DefaultTask() {

    }

    public DefaultTask(Long id, Site site, DefRootExtractor extractor) {
        this.id = id;
        this.site = site;
        this.defExtractor = extractor;
    }

    @Override
    public Long getId() {
        return id;
    }

    public DefaultTask setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public Site getSite() {
        return site;
    }

    public DefaultTask setSite(Site site) {
        this.site = site;
        return this;
    }

    @Override
    public ModelExtractor modelExtractor() {
        return new ConfigurableModelExtractor(defExtractor);
    }

    public DefRootExtractor getDefExtractor() {
        return defExtractor;
    }

    public DefaultTask setDefExtractor(DefRootExtractor defExtractor) {
        this.defExtractor = defExtractor;
        return this;
    }
}
