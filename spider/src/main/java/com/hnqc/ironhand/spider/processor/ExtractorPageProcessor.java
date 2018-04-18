package com.hnqc.ironhand.spider.processor;

import com.hnqc.ironhand.spider.ExtractorTask;
import com.hnqc.ironhand.spider.Page;
import com.hnqc.ironhand.spider.ResultItem;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.extractor.ModelExtractor;

import java.util.List;

/**
 * mapped model page processor
 *
 * @author zido
 * @date 2018/04/12
 */
public class ExtractorPageProcessor implements PageProcessor {
    @Override
    public ResultItem process(Task task, Page page) {
        ResultItem item = new ResultItem();
        if (!(task instanceof ExtractorTask)) {
            return null;
        }
        ExtractorTask extractorTask = (ExtractorTask) task;
        ModelExtractor extractor = extractorTask.getModelExtractor();
        List<String> links = extractor.extractLinks(page);
        Object result = extractor.extract(page);
        if (result == null) {
            return null;
        }

        //TODO page processor
        page.putField(extractor.getModelExtractor().getName(), result);
        if (links != null) {
            results.addAll(links);
        }
        if (page.getResultItem().getAll().size() == 0) {
            page.getResultItem().setSkip(true);
        }
        return results;
    }
}
