package site.zido.elise.processor;

public interface CrawlResult {

    void cancel();

    int count();

    default boolean isEmpty() {
        return count() == 0;
    }

}
