package site.zido.elise.processor;

import site.zido.elise.task.Task;

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

    /**
     * Next result item.
     *
     * @param task the task
     * @param item the item
     * @return the result item
     */
    ResultItem next(Task task, ResultItem item);

    /**
     * Has next boolean.
     *
     * @param task the task
     * @param item the item
     * @return the boolean
     */
    boolean hasNext(Task task, ResultItem item);

    /**
     * First result item.
     *
     * @param task the task
     * @return the result item
     */
    ResultItem first(Task task);

    /**
     * Size int.
     *
     * @param task the task
     * @return the int
     */
    int size(Task task);
}
