package com.hnqc.ironhand;

import com.hnqc.ironhand.extractor.ModelExtractor;

/**
 * ExtractorTask
 *
 * @author zido
 * @date 2018/04/18
 */
public interface ExtractorTask extends Task {

    /**
     * get the model extractor
     *
     * @return extractors
     */
    ModelExtractor modelExtractor();
}
