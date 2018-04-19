package com.hnqc.ironhand;

import com.hnqc.ironhand.configurable.ConfigurableModelExtractor;
import com.hnqc.ironhand.configurable.DefRootExtractor;
import com.hnqc.ironhand.extractor.ModelExtractor;

/**
 * 可用于分布式传输的task
 * <br>
 * 用作信息载体，仅携带必要信息
 *
 * @author zido
 * @date 2018/04/16
 */
public class DistributedTask implements ExtractorTask {
    private Long id;
    private Site site;
    private DefRootExtractor defExtractor;

    public DistributedTask(ExtractorTask task) {
        this.id = task.getId();
        this.site = task.getSite();
        ModelExtractor modelExtractor = task.getModelExtractor();
        if (modelExtractor != null) {
            if (modelExtractor instanceof ConfigurableModelExtractor) {
                this.defExtractor = ((ConfigurableModelExtractor) modelExtractor).getDefRootExtractor();
            }
        }
    }

    public DistributedTask(Long id, Site site, DefRootExtractor extractor) {
        this.id = id;
        this.site = site;
        this.defExtractor = extractor;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @Override
    public ModelExtractor getModelExtractor() {
        return new ConfigurableModelExtractor(defExtractor);
    }

    public DistributedTask setDefExtractor(DefRootExtractor defExtractor) {
        this.defExtractor = defExtractor;
        return this;
    }

    public DefRootExtractor getDefExtractor() {
        return defExtractor;
    }
}
