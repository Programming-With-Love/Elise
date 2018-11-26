package site.zido.elise.processor;

/**
 * The type Saved craw result.
 *
 * @author zido
 */
public class SavedCrawResult implements CrawlHandler {
    private static final long serialVersionUID = 391732165604651548L;

    /**
     * Instantiates a new Saved craw result.
     */
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
