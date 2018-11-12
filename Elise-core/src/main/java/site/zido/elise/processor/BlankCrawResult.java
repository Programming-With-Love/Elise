package site.zido.elise.processor;

public class BlankCrawResult implements CrawlResult {
    @Override
    public void cancel() {

    }

    @Override
    public int count() {
        return 0;
    }
}
