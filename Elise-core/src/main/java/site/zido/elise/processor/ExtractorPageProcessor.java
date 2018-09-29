package site.zido.elise.processor;

import site.zido.elise.*;
import site.zido.elise.extractor.ModelExtractor;
import site.zido.elise.scheduler.TaskScheduler;

import java.util.List;

/**
 * model page processor with extractor,
 *
 * @author zido
 */
public class ExtractorPageProcessor implements PageProcessor {
    @Override
    public List<ResultItem> process(Task task, Page page, RequestPutter putter) {
        ModelExtractor extractor = task.modelExtractor();
        List<String> links = extractor.extractLinks(page);
        if (links != null) {
            for (String link : links) {
                putter.pushRequest(task, new Request(link));
            }
        }
        return extractor.extract(page);
    }
}
