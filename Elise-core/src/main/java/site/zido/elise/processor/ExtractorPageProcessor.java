package site.zido.elise.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.*;
import site.zido.elise.extractor.ModelExtractor;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * model page processor with extractor,
 *
 * @author zido
 */
public class ExtractorPageProcessor implements PageProcessor {
    private static Logger LOGGER = LoggerFactory.getLogger(ExtractorPageProcessor.class);

    @Override
    public List<ResultItem> process(Task task, Page page, RequestPutter putter) {
        ModelExtractor extractor = task.modelExtractor();
        List<String> links = extractor.extractLinks(page);
        if (links != null) {
            for (String link : links) {
                try {
                    putter.pushRequest(task, new Request(link)).get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    LOGGER.error("error:", e.getCause());
                }
            }
        }
        return extractor.extract(page);
    }
}
