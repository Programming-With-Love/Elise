package com.hnqc.ironhand.scheduler;

import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Task;

public interface DuplicateRemover {

    boolean isDuplicate(Request request, Task task);

    void resetDuplicateCheck(Task task);

    int getTotalRequestsCount(Task task);
}
