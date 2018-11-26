package site.zido.elise.processor;

import java.util.concurrent.Future;

/**
 * The type Future craw result.
 *
 * @param <T> the type parameter
 * @author zido
 */
public class FutureCrawResult<T> implements CrawlHandler {
    private static final long serialVersionUID = 2376315568978974152L;
    private Future<T> future;

    /**
     * Instantiates a new Future craw result.
     *
     * @param future the future
     */
    public FutureCrawResult(Future<T> future) {
        this.future = future;
    }

    @Override
    public void cancel() {
        future.cancel(true);
    }

    @Override
    public int resultCount() {
        return 0;
    }
}
