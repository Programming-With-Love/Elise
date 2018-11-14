package site.zido.elise.processor;

import java.io.Serializable;

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

    default boolean isEmpty() {
        return resultCount() == 0;
    }

}
