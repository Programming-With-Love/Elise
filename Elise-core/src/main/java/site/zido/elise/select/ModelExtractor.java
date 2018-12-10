package site.zido.elise.select;

import site.zido.elise.processor.ProcessorEventListener;
import site.zido.elise.processor.Saver;
import site.zido.elise.select.api.SelectableResponse;

import java.util.List;
import java.util.Set;

/**
 * model extractor
 *
 * @author zido
 */
public interface ModelExtractor {

    /**
     * Extract result item.
     *
     * @param response  the response
     * @param saver
     * @param listeners
     * @return the result item
     */
    List<String> extract(SelectableResponse response, Saver saver, Set<ProcessorEventListener> listeners);

}
