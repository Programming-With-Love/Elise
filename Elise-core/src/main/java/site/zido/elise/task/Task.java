package site.zido.elise.task;

import site.zido.elise.custom.Config;
import site.zido.elise.select.configurable.ModelExtractor;

/**
 * Task interface
 *
 * @author zido
 */
public interface Task {
    /**
     * Get task id
     *
     * @return id id
     */
    long getId();

    /**
     * get the model extractor
     *
     * @return extractors model extractor
     */
    ModelExtractor modelExtractor();

    /**
     * Gets config.
     *
     * @return the config
     */
    Config getConfig();
}
