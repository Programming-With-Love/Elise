package site.zido.elise.processor;

import site.zido.elise.select.Fragment;
import site.zido.elise.task.Task;

import java.util.List;
import java.util.Map;

/**
 * The type Blank saver.
 *
 * @author zido
 */
public class BlankSaver implements Saver {
    @Override
    public void save(ResultItem resultItem, Task task) {
        Map<String, List<Fragment>> all = resultItem.getAll();
        for (Map.Entry<String, List<Fragment>> entry : all.entrySet()) {
            System.out.println(entry.getKey() + ":\t" + entry.getValue());
            //LOGGER.debug(entry.getKey() + ":\t" + entry.getValue());
        }
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
