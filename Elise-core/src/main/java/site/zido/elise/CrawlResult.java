package site.zido.elise;

import site.zido.elise.saver.BlankSaver;
import site.zido.elise.saver.Saver;

import java.util.Iterator;

public class CrawlResult implements Iterable<ResultItem> {
    private Saver saver;
    private Task task;

    public CrawlResult(Task task, Saver saver) {
        this.task = task;
        this.saver = saver;
    }

    public static CrawlResult blank() {
        return new CrawlResult(null, new BlankSaver());
    }

    public int size() {
        return saver.size(task);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public Iterator<ResultItem> iterator() {
        return new ItemIter();
    }

    private class ItemIter implements Iterator<ResultItem> {

        private ResultItem current;

        private ItemIter() {
            current = saver.first(task);
        }

        @Override
        public boolean hasNext() {
            return saver.hasNext(task, current);
        }

        @Override
        public ResultItem next() {
            current = saver.next(task, current);
            return current;
        }
    }
}
