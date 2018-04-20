package com.hnqc.ironhand.common;

import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Task;
import com.hnqc.ironhand.message.AbstractDuplicateRemovedScheduler;
import com.hnqc.ironhand.scheduler.DuplicationProcessor;
import com.hnqc.ironhand.scheduler.MonitorableScheduler;
import com.hnqc.ironhand.utils.ValidateUtils;

/**
 * Simple distributed task scheduling abstract class
 *
 * @author zido
 * @date 2018/40/12
 */
public abstract class AbstractDistributedScheduler extends AbstractDuplicateRemovedScheduler
        implements MonitorableScheduler, DuplicationProcessor {

    private static final String QUEUE_PREFIX = "queue_";

    private static final String SET_PREFIX = "set_";

    private static final String ITEM_PREFIX = "item_";

    public AbstractDistributedScheduler(DuplicationProcessor duplicationProcessor) {
        super(duplicationProcessor);
    }


    protected boolean checkForAdditionalInfo(Request request) {
        if (request == null) {
            return false;
        }

        if (!request.getHeaders().isEmpty() || !request.getCookies().isEmpty()) {
            return true;
        }

        if (!ValidateUtils.isEmpty(request.getCharset()) || !ValidateUtils.isEmpty(request.getMethod())) {
            return true;
        }

        if (request.getRequestBody() != null) {
            return true;
        }

        return request.getExtras() != null && !request.getExtras().isEmpty();
    }

    protected String getSetKey(Task task) {
        return SET_PREFIX + task.getId();
    }

    protected String getQueueKey(Task task) {
        return QUEUE_PREFIX + task.getId();
    }

    protected String getItemKey(Task task) {
        return ITEM_PREFIX + task.getId();
    }

}
