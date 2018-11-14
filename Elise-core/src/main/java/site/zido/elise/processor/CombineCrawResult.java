package site.zido.elise.processor;

import java.util.HashSet;

public class CombineCrawResult extends HashSet<CrawlHandler> implements CrawlHandler {

    private static final long serialVersionUID = -522255722562725688L;

    @Override
    public boolean add(CrawlHandler crawlHandler) {
        if (crawlHandler instanceof CombineCrawResult) {
            return super.addAll((CombineCrawResult) crawlHandler);
        }
        return super.add(crawlHandler);
    }

    @Override
    public void cancel() {
        for (CrawlHandler result : this) {
            result.cancel();
        }
    }

    @Override
    public int resultCount() {
        return this.size();
    }
}
