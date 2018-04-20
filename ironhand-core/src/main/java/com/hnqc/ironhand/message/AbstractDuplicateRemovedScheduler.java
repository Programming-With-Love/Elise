package com.hnqc.ironhand.message;

import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Task;
import com.hnqc.ironhand.scheduler.DuplicationProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract Duplicate Removed Scheduler
 *
 * @author zido
 * @date 2018/43/12
 */
public abstract class AbstractDuplicateRemovedScheduler implements TaskScheduler {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private DuplicationProcessor duplicationProcessor;

    public AbstractDuplicateRemovedScheduler(DuplicationProcessor duplicationProcessor) {
        this.duplicationProcessor = duplicationProcessor;
    }


    @Override
    public void download(Task task, Request request) {
        logger.trace("get a candidate url {}", request.getUrl());
        if (shouldReserved(request)
                || noNeedToRemoveDuplicate(request)
                || !duplicationProcessor.isDuplicate(request, task)) {
            logger.debug("push to queue {}", request.getUrl());
            pushWhenNoDuplicate(request, task);
        }
    }

    private boolean shouldReserved(Request request) {
        return request.getExtra(Request.CYCLE_TRIED_TIMES) != null;
    }

    private boolean noNeedToRemoveDuplicate(Request request) {
        return "post".equalsIgnoreCase(request.getMethod());
    }

    /**
     * Specific insert logic implementation,
     * This method is called after removing duplicate data
     *
     * @param request request
     * @param task    task
     */
    protected abstract void pushWhenNoDuplicate(Request request, Task task);

    public int getTotalRequestsCount(Task task) {
        return duplicationProcessor.getTotalRequestsCount(task);
    }
}
