package site.zido.elise.processor;

import java.util.HashSet;

public class CombineCrawResult extends HashSet<CrawlResult> implements CrawlResult {

    private static final long serialVersionUID = -522255722562725688L;

    @Override
    public boolean add(CrawlResult crawlResult) {
        if (crawlResult instanceof CombineCrawResult) {
            return super.addAll((CombineCrawResult) crawlResult);
        }
        return super.add(crawlResult);
    }

    @Override
    public void cancel() {
        for (CrawlResult result : this) {
            result.cancel();
        }
    }

    @Override
    public int count() {
        return this.size();
    }
}
