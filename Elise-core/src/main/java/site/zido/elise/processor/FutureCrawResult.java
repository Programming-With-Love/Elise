package site.zido.elise.processor;

import java.util.concurrent.Future;

public class FutureCrawResult<T> implements CrawlResult {
    private static final long serialVersionUID = 2376315568978974152L;
    private Future<T> future;

    public FutureCrawResult(Future<T> future) {
        this.future = future;
    }

    @Override
    public void cancel() {
        future.cancel(true);
    }

    @Override
    public int count() {
        return 0;
    }
}
