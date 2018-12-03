package site.zido.elise.select.configurable;

import site.zido.elise.ResultItem;
import site.zido.elise.custom.Config;
import site.zido.elise.http.Response;

import java.util.List;
import java.util.Set;

/**
 * model extractor
 *
 * @author zido
 */
public interface ModelExtractor {

    Config getConfig();

    /**
     * Extract result item.
     *
     * @param response the response
     * @return the result item
     */
    List<ResultItem> extract(Response response);

    /**
     * Extract links list.
     *
     * @param response the response
     * @return the list
     */
    Set<String> extractLinks(Response response);
}
