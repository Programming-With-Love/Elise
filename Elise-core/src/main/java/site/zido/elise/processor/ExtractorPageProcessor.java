package site.zido.elise.processor;

import site.zido.elise.Page;
import site.zido.elise.Request;
import site.zido.elise.ResultItem;
import site.zido.elise.Task;
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
    public List<ResultItem> process(Task task, Page page, TaskScheduler putter) {
        ModelExtractor extractor = task.modelExtractor();
        List<String> links = extractor.extractLinks(page);
        if (links != null) {
            for (String link : links) {
                putter.pushRequest(task.getId(), new Request(link));
            }
        }
        return extractor.extract(page);
    }
}
