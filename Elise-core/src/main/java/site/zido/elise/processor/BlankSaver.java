package site.zido.elise.processor;

import site.zido.elise.ResultItem;
import site.zido.elise.Task;

/**
 * The type Blank saver.
 *
 * @author zido
 */
public class BlankSaver implements Saver {
    @Override
    public void save(ResultItem resultItem, Task task) {

    }

    @Override
    public ResultItem next(Task task, ResultItem item) {
        return null;
    }

    @Override
    public boolean hasNext(Task task, ResultItem item) {
        return false;
    }

    @Override
    public ResultItem first(Task task) {
        return null;
    }

    @Override
    public int size(Task task) {
        return 0;
    }
}
