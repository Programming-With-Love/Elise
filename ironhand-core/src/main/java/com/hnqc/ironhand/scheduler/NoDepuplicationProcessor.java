package com.hnqc.ironhand.scheduler;

import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Task;

/**
 * NoDepuplicationProcessor
 *
 * @author zido
 * @date 2018/04/27
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
