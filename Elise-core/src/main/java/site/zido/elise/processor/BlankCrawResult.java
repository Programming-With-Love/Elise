package site.zido.elise.processor;

/**
 * The type Blank craw result.
 *
 * @author zido
 */
public class BlankCrawResult implements CrawlHandler {
    private static final long serialVersionUID = -25263879752756758L;

    @Override
    public void cancel() {

    }

    @Override
    public int resultCount() {
        return 0;
    }
}
