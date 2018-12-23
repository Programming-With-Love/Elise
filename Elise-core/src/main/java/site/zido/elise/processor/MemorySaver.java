package site.zido.elise.processor;

import site.zido.elise.task.Task;
import site.zido.elise.utils.ValidateUtils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Console saver.
 *
 * @author zido
 */
public class MemorySaver extends BlankSaver implements Saver {
    private Map<Long, List<ResultItem>> cup = new ConcurrentHashMap<>();

    public MemorySaver(){

    }
    public MemorySaver(PrintStream stream) {
        super(stream);
    }

    /**
     * Gets cup.
     *
     * @return the cup
     */
    public Map<Long, List<ResultItem>> getCup() {
        return cup;
    }

    @Override
    public void save(ResultItem resultItem, Task task) {
        List<ResultItem> resultItems = cup.computeIfAbsent(task.getId(), k -> new ArrayList<>());
        resultItems.add(resultItem);
        super.save(resultItem, task);
    }

    @Override
    public ResultItem next(Task task, ResultItem item) {
        List<ResultItem> resultItems = cup.get(task.getId());
        if (ValidateUtils.isEmpty(resultItems)) {
            return null;
        }
        if (item == null) {
            return resultItems.get(0);
        }
        int i = resultItems.indexOf(item);
        if (i >= 0 && resultItems.size() - 1 > i) {
            return resultItems.get(i + 1);
        }
        return null;
    }

    @Override
    public boolean hasNext(Task task, ResultItem item) {
        return next(task, item) != null;
    }

    @Override
    public ResultItem first(Task task) {
        return next(task, null);
    }

    @Override
    public int size(Task task) {
        return cup.computeIfAbsent(task.getId(), k -> new ArrayList<>()).size();
    }
}
