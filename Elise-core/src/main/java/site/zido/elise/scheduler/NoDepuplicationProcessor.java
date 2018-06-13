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
    public boolean isDuplicate(Request request, Task task) {
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
