package site.zido.elise.scheduler;

import site.zido.elise.Request;
import site.zido.elise.Task;

/**
 * NoDepuplicationProcessor
 *
 * @author zido
 */
public class NoDepuplicationProcessor implements DuplicationProcessor {
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
