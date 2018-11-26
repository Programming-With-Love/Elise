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
     * @return id id
     */
    Long getId();

    /**
     * Get website configuration
     *
     * @return site site
     */
    Site getSite();

    /**
     * get the model extractor
     *
     * @return extractors model extractor
     */
    ModelExtractor modelExtractor();
}
