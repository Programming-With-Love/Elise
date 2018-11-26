package site.zido.elise.processor;

import java.io.Serializable;

/**
 * The interface Crawl handler.
 *
 * @author zido
 */
public interface CrawlHandler extends Serializable {

    /**
     * cancel this task
     */
    void cancel();

    /**
     * get the number of results captured
     *
     * @return the count number
     */
    int resultCount();

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    default boolean isEmpty() {
        return resultCount() == 0;
    }

}
