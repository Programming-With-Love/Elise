package com.hnqc.ironhand.spider.downloader;

import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.extractor.ModelExtractor;

import java.util.ArrayList;
import java.util.List;

/**
 * Downloader Task Adapter
 *
 * @author zido
 * @date 2018/04/18
 */
public abstract class AbstractDownloaderTaskAdapter implements Task {
    private List<ModelExtractor> modelExtractors = new ArrayList<>();

    public AbstractDownloaderTaskAdapter() {
    }

    public AbstractDownloaderTaskAdapter(List<ModelExtractor> extractors) {
        this.modelExtractors = extractors;
    }

    @Override
    public List<ModelExtractor> getModelExtractors() {
        return modelExtractors;
    }
}
