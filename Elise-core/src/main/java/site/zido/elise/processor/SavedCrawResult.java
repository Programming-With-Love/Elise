package site.zido.elise.processor;

public class SavedCrawResult implements CrawlResult {
    private static final long serialVersionUID = 391732165604651548L;

    public SavedCrawResult() {
    }

    @Override
    public void cancel() {

    }

    @Override
    public int count() {
        return 1;
    }
}
