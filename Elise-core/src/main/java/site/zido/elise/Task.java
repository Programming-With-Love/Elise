package site.zido.elise;

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
     * @return id
     */
    Long getId();

    /**
     * Get website configuration
     *
     * @return site
     */
    Site getSite();

    /**
     * get the model extractor
     *
     * @return extractors
     */
    ModelExtractor modelExtractor();
}
