package site.zido.elise;

import site.zido.elise.extractor.ModelExtractor;

/**
 * ExtractorTask
 *
 * @author zido
 */
public interface ExtractorTask extends Task {

    /**
     * get the model extractor
     *
     * @return extractors
     */
    ModelExtractor modelExtractor();
}
