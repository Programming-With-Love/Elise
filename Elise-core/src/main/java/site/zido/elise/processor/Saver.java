package site.zido.elise.processor;

import site.zido.elise.ResultItem;
import site.zido.elise.Task;

/**
 * 结果输出处理器
 *
 * @author zido
 */
public interface Saver {
    /**
     * 结果处理
     *
     * @param resultItem 结果集
     * @param task       任务
     */
    void save(ResultItem resultItem, Task task);

    ResultItem next(Task task, ResultItem item);

    boolean hasNext(Task task, ResultItem item);

    ResultItem first(Task task);

    int size(Task task);
}
