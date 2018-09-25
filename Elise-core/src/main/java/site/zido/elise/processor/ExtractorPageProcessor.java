package site.zido.elise.processor;

import site.zido.elise.*;
import site.zido.elise.extractor.ModelExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.matcher.NumberExpressMatcher;
import site.zido.elise.scheduler.TaskScheduler;

import java.util.List;

/**
 * model page processor with extractor,
 * and Must ensure that task{@link Task} is ExtractorTask{@link ExtractorTask} subclass
 *
 * @author zido
 */
public class ExtractorPageProcessor implements PageProcessor {
    @Override
    public List<ResultItem> process(Task task, Page page, TaskScheduler putter) {
        ExtractorTask extractorTask = (ExtractorTask) task;
        ModelExtractor extractor = extractorTask.modelExtractor();
        List<String> links = extractor.extractLinks(page);
        if (links != null) {
            for (String link : links) {
                putter.pushRequest(task, new Request(link));
            }
        }
        return extractor.extract(page);
    }
}
