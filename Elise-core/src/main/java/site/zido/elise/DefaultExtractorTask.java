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
public class DefaultExtractorTask implements Task {
    private Long id;
    private Site site;
    private DefRootExtractor defExtractor;

    public DefaultExtractorTask() {

    }

    public DefaultExtractorTask(Task task) {
        this.id = task.getId();
        this.site = task.getSite();
        ModelExtractor modelExtractor = task.modelExtractor();
        if (modelExtractor != null) {
            if (modelExtractor instanceof ConfigurableModelExtractor) {
                this.defExtractor = ((ConfigurableModelExtractor) modelExtractor).getDefRootExtractor();
            }
        }
    }

    public DefaultExtractorTask(Long id, Site site, DefRootExtractor extractor) {
        this.id = id;
        this.site = site;
        this.defExtractor = extractor;
    }

    @Override
    public Long getId() {
        return id;
    }

    public DefaultExtractorTask setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public Site getSite() {
        return site;
    }

    public DefaultExtractorTask setSite(Site site) {
        this.site = site;
        return this;
    }

    @Override
    public ModelExtractor modelExtractor() {
        return new ConfigurableModelExtractor(defExtractor);
    }

    public DefaultExtractorTask setDefExtractor(DefRootExtractor defExtractor) {
        this.defExtractor = defExtractor;
        return this;
    }

    public DefRootExtractor getDefExtractor() {
        return defExtractor;
    }
}
