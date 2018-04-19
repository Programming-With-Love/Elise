package com.hnqc.ironhand.scheduler;

import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象去重任务配置
 *
 * @author zido
 * @date 2018/43/12
 */
public abstract class AbstractDuplicateRemovedScheduler implements Scheduler {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private DuplicateRemover duplicatedRemover = new HashSetDuplicateRemover();

    public DuplicateRemover getDuplicateRemover() {
        return duplicatedRemover;
    }

    public AbstractDuplicateRemovedScheduler setDuplicateRemover(DuplicateRemover duplicatedRemover) {
        this.duplicatedRemover = duplicatedRemover;
        return this;
    }

    @Override
    public boolean push(Request request, Task task) {
        logger.trace("get a candidate url {}", request.getUrl());
        if (shouldReserved(request) || noNeedToRemoveDuplicate(request) || !duplicatedRemover.isDuplicate(request, task)) {
            logger.debug("push to queue {}", request.getUrl());
            pushWhenNoDuplicate(request, task);
            return true;
        } else {
            return false;
        }
    }

    protected boolean shouldReserved(Request request) {
        return request.getExtra(Request.CYCLE_TRIED_TIMES) != null;
    }

    protected boolean noNeedToRemoveDuplicate(Request request) {
        return "post".equalsIgnoreCase(request.getMethod());
    }

    protected void pushWhenNoDuplicate(Request request, Task task) {

    }
}
