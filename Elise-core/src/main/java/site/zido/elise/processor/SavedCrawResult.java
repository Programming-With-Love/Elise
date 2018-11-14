package site.zido.elise.processor;

public class SavedCrawResult implements CrawlHandler {
    private static final long serialVersionUID = 391732165604651548L;

    public SavedCrawResult() {
    }

    @Override
    public void cancel() {

    }

    @Override
    public int resultCount() {
        return 1;
    }
}
