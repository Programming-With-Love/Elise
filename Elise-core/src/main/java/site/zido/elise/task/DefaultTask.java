package site.zido.elise.task;

import site.zido.elise.custom.Config;
import site.zido.elise.select.configurable.ConfigurableModelExtractor;
import site.zido.elise.select.configurable.ModelExtractor;

/**
 * default extractor task
 * <br>
 *
 * @author zido
 */
public class DefaultTask implements Task {
    private Long id;
    private ModelExtractor extractor;
    private Config config;

    /**
     * Instantiates a new Default task.
     */
    public DefaultTask() {

    }

    /**
     * Instantiates a new Default task.
     *
     * @param id        the id
     * @param extractor the extractor
     */
    public DefaultTask(Long id, ModelExtractor extractor, Config config) {
        this.id = id;
        this.extractor = extractor;
        this.config = config;
    }

    @Override
    public long getId() {
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
    public ModelExtractor modelExtractor() {
        return extractor;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    public DefaultTask setConfig(Config config) {
        this.config = config;
        return this;
    }

    /**
     * Gets def extractor.
     *
     * @return the def extractor
     */
    public ModelExtractor getExtractor() {
        return extractor;
    }

    /**
     * Sets def extractor.
     *
     * @param extractor the def extractor
     * @return the def extractor
     */
    public DefaultTask setExtractor(ConfigurableModelExtractor extractor) {
        this.extractor = extractor;
        return this;
    }
}
