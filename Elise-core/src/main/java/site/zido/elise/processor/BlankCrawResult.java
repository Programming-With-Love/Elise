package site.zido.elise.processor;

public class BlankCrawResult implements CrawlResult {
    private static final long serialVersionUID = -25263879752756758L;

    @Override
    public void cancel() {

    }

    @Override
    public int count() {
        return 0;
    }
}
