package site.zido.elise.select.configurable;

import site.zido.elise.Page;
import site.zido.elise.ResultItem;

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
     * @param page the page
     * @return the result item
     */
    List<ResultItem> extract(Page page);

    /**
     * Extract links list.
     *
     * @param page the page
     * @return the list
     */
    Set<String> extractLinks(Page page);
}
