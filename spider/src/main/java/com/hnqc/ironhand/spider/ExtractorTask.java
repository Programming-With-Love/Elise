package com.hnqc.ironhand.spider;

import com.hnqc.ironhand.spider.extractor.ModelExtractor;

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
    ModelExtractor getModelExtractor();
}
