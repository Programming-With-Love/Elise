package site.zido.elise.saver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.ResultItem;
import site.zido.elise.Task;
import site.zido.elise.utils.ValidateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Console saver.
 *
 * @author zido
 */
public class MemorySaver implements Saver {
    private static Logger LOGGER = LoggerFactory.getLogger(MemorySaver.class);
    private Map<Long, List<ResultItem>> cup = new ConcurrentHashMap<>();

    @Override
    public void save(ResultItem resultItem, Task task) {
        List<ResultItem> resultItems = cup.computeIfAbsent(task.getId(), k -> new ArrayList<>());
        resultItems.add(resultItem);
        Map<String, List<String>> all = resultItem.getAll();
        for (Map.Entry<String, List<String>> entry : all.entrySet()) {
            LOGGER.debug(entry.getKey() + ":\t" + entry.getValue());
        }
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
