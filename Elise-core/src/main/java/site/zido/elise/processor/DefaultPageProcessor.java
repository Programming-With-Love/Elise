package site.zido.elise.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.Page;
import site.zido.elise.ResultItem;
import site.zido.elise.Task;
import site.zido.elise.select.configurable.ModelExtractor;

import java.util.List;

/**
 * model page processor with extractor,
 *
 * @author zido
 */
public class DefaultPageProcessor implements PageProcessor {
    @Override
    public ItemLinksModel process(Task task, Page page) {
        ModelExtractor extractor = task.modelExtractor();
        List<String> links = extractor.extractLinks(page);
        List<ResultItem> extract = extractor.extract(page);
        return new ItemLinksModel(extract,links);
    }
}
