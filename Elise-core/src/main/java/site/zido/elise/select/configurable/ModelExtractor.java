package site.zido.elise.select.configurable;

import site.zido.elise.ResultItem;
import site.zido.elise.http.impl.DefaultResponse;

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
     * @param response the response
     * @return the result item
     */
    List<ResultItem> extract(DefaultResponse response);

    /**
     * Extract links list.
     *
     * @param response the response
     * @return the list
     */
    Set<String> extractLinks(DefaultResponse response);
}
