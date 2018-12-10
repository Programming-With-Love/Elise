package site.zido.elise.processor;

import site.zido.elise.task.Task;

import java.util.EventListener;

/**
 * The interface Processor event listener.
 *
 * @author zido
 */
public interface ProcessorEventListener extends EventListener {
    /**
     * On save success.
     *
     * @param task        the task
     * @param resultItem the result items
     */
    default void onSaveSuccess(Task task, ResultItem resultItem) {
    }

    /**
     * On save error.
     *
     * @param task        the task
     * @param resultItem the result items
     */
    default void onSaveError(Task task, ResultItem resultItem) {
    }
}
