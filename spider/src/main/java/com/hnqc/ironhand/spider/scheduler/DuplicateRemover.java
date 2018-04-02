package com.hnqc.ironhand.spider.scheduler;

import com.hnqc.ironhand.spider.Request;
import com.hnqc.ironhand.spider.Task;

public interface DuplicateRemover {

    boolean isDuplicate(Request request, Task task);

    void resetDuplicateCheck(Task task);

    int getTotalRequestsCount(Task task);
}
