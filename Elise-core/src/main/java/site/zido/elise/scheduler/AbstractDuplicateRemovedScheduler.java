package site.zido.elise.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.Request;
import site.zido.elise.Task;

/**
 * Abstract Duplicate Removed Scheduler
 *
 * @author zido
 */
public abstract class AbstractDuplicateRemovedScheduler implements TaskScheduler {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private DuplicationProcessor duplicationProcessor;

    public AbstractDuplicateRemovedScheduler(DuplicationProcessor duplicationProcessor) {
        this.duplicationProcessor = duplicationProcessor;
    }

    @Override
    public boolean pushRequest(long taskId, Request request) {
        logger.debug("get a candidate url {}", request.getUrl());
        if (shouldReserved(request)
                || noNeedToRemoveDuplicate(request)
                || !duplicationProcessor.isDuplicate(request, taskId)) {
            logger.debug("push to queue {}", request.getUrl());
            pushWhenNoDuplicate(request, taskId);
            return true;
        }
        return false;
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
     * @param taskId  task
     */
    protected abstract void pushWhenNoDuplicate(Request request, long taskId);


    public int getTotalRequestsCount(Task task) {
        return duplicationProcessor.getTotalRequestsCount(task);
    }
}
