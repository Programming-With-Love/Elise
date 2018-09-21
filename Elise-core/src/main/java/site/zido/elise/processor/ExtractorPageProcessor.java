package site.zido.elise.processor;

import site.zido.elise.*;
import site.zido.elise.extractor.ModelExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.scheduler.TaskScheduler;

import java.util.List;

/**
 * model page processor with extractor,
 * and Must ensure that task{@link Task} is ExtractorTask{@link ExtractorTask} subclass
 *
 * @author zido
 */
public class ExtractorPageProcessor implements PageProcessor {
    private Logger logger = LoggerFactory.getLogger(ExtractorPageProcessor.class);

    @Override
    public ResultItem process(Task task, Page page, TaskScheduler putter) {
        if (!(task instanceof ExtractorTask)) {
            logger.error("no extractor in task[id = {}] and stop this task", task.getId());
            return null;
        }
        int statusCode = page.getStatusCode();

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
