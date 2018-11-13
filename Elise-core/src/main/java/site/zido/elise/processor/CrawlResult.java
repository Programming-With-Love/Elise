package site.zido.elise.processor;

import java.io.Serializable;

public interface CrawlResult extends Serializable {

    void cancel();

    int count();

    default boolean isEmpty() {
        return count() == 0;
    }

}
