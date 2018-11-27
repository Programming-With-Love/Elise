package site.zido.elise.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.ResultItem;
import site.zido.elise.Task;
import site.zido.elise.http.Response;
import site.zido.elise.select.configurable.ModelExtractor;
import site.zido.elise.utils.EventUtils;
import site.zido.elise.utils.ValidateUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * model page processor with extractor,
 *
 * @author zido
 */
public class DefaultResponseHandler implements ListenableResponseHandler {
    private static Logger LOGGER = LoggerFactory.getLogger(DefaultResponseHandler.class);

    private Set<ProcessorEventListener> listeners = new HashSet<>();
    private Saver saver;

    /**
     * Instantiates a new Default response handler.
     *
     * @param saver the saver
     */
    public DefaultResponseHandler(Saver saver) {
        this.saver = saver;
    }

    @Override
    public Set<String> process(Task task, Response response) {
        ModelExtractor extractor = task.modelExtractor();
        Set<String> links = extractor.extractLinks(response);
        List<ResultItem> resultItems = extractor.extract(response);
        if (!ValidateUtils.isEmpty(resultItems)) {
            for (ResultItem resultItem : resultItems) {
                if (resultItem != null) {
                    try {
                        saver.save(resultItem, task);
                        EventUtils.mustNotifyListeners(listeners, listener -> listener.onSaveSuccess(task, resultItem));
                    } catch (Throwable e) {
                        EventUtils.mustNotifyListeners(listeners, listener -> listener.onSaveError(task, resultItem));
                        LOGGER.error("saver have made a exception", e);
                    }
                }
            }
        } else {
            LOGGER.info("response not find anything, response {}", response.getUrl());
        }
        return links;
    }

    @Override
    public void addEventListener(ProcessorEventListener listener) {
        listeners.add(listener);
    }
}
