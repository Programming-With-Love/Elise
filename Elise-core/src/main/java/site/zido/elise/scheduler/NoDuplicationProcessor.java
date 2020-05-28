package site.zido.elise.scheduler;

import site.zido.elise.http.Request;
import site.zido.elise.task.Task;

/**
 * NoDepuplicationProcessor
 *
 * @author zido
 */
public class NoDuplicationProcessor implements DuplicationProcessor {
    @Override
    public boolean isDuplicate(Task task, Request request) {
        return false;
    }

    @Override
    public void resetDuplicateCheck(Task task) {

    }

    @Override
    public int getTotalRequestsCount(Task task) {
        return 0;
    }
}
