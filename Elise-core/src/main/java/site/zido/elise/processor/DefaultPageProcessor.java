package site.zido.elise.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.Page;
import site.zido.elise.ResultItem;
import site.zido.elise.Task;
import site.zido.elise.select.configurable.ModelExtractor;
import site.zido.elise.utils.ValidateUtils;

import java.util.List;

/**
 * model page processor with extractor,
 *
 * @author zido
 */
public class DefaultPageProcessor implements PageProcessor {
    private static Logger LOGGER = LoggerFactory.getLogger(DefaultPageProcessor.class);

    private Saver saver;

    public DefaultPageProcessor(Saver saver) {
        this.saver = saver;
    }

    @Override
    public List<String> process(Task task, Page page) {
        ModelExtractor extractor = task.modelExtractor();
        List<String> links = extractor.extractLinks(page);
        List<ResultItem> resultItems = extractor.extract(page);
        if (!ValidateUtils.isEmpty(resultItems)) {
            for (ResultItem resultItem : resultItems) {
                if (resultItem != null) {
                    try {
                        saver.save(resultItem, task);
                    } catch (Throwable e) {
                        LOGGER.error("saver have made a exception", e);
                    }
                }
            }
        } else {
            LOGGER.info("page not find anything, page {}", page.getUrl());
        }
        return links;
    }
}
