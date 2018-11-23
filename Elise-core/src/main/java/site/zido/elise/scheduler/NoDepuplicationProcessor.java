package site.zido.elise.scheduler;

import site.zido.elise.http.impl.DefaultRequest;
import site.zido.elise.Task;

/**
 * NoDepuplicationProcessor
 *
 * @author zido
 */
public class NoDepuplicationProcessor implements DuplicationProcessor {
    @Override
    public boolean isDuplicate(Task task, DefaultRequest request) {
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
