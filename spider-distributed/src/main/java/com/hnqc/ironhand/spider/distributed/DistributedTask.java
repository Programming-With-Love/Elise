package com.hnqc.ironhand.spider.distributed;

import com.hnqc.ironhand.spider.Site;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.distributed.configurable.DefRootExtractor;
import com.hnqc.ironhand.spider.distributed.configurable.ConfigurableModelExtractor;
import com.hnqc.ironhand.spider.extractor.ModelExtractor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 可用于分布式传输的task
 * <br>
 * 用作信息载体，仅携带必要信息
 *
 * @author zido
 * @date 2018/04/16
 */
public class DistributedTask implements Task {
    private Long id;
    private Site site;
    private List<DefRootExtractor> defExtractors;

    public DistributedTask(Task task) {
        this.id = task.getId();
        this.site = task.getSite();
        List<ModelExtractor> modelExtractors = task.getModelExtractors();
        if (modelExtractors != null && modelExtractors.size() > 0) {
            this.defExtractors = modelExtractors.stream()
                    .filter(modelExtractor -> modelExtractor instanceof ConfigurableModelExtractor)
                    .map(modelExtractor -> ((ConfigurableModelExtractor) modelExtractor).getDefRootExtractor())
                    .collect(Collectors.toList());
        }
    }

    public DistributedTask(Long id, Site site, List<DefRootExtractor> extractors) {
        this.id = id;
        this.site = site;
        this.defExtractors = extractors;
    }

    public DistributedTask(Long id, Site site, DefRootExtractor... defRootExtractor) {
        this(id, site, Arrays.asList(defRootExtractor));
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
    public List<ConfigurableModelExtractor> getModelExtractors() {
        return defExtractors.stream().map(ConfigurableModelExtractor::new).collect(Collectors.toList());
    }

    public DistributedTask setDefExtractors(List<DefRootExtractor> defExtractors) {
        this.defExtractors = defExtractors;
        return this;
    }
}
