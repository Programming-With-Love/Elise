package site.zido.elise.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.Request;
import site.zido.elise.Site;
import site.zido.elise.Task;
import site.zido.elise.processor.CrawlResult;
import site.zido.elise.utils.UrlUtils;

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
    public CrawlResult pushRequest(Task task, Request request) {
        logger.debug("get a candidate url {}", request.getUrl());
        if (shouldReserved(request)
                || noNeedToRemoveDuplicate(request)
                || !duplicationProcessor.isDuplicate(task, request)) {
            logger.debug("push to queue {}", request.getUrl());
            Site site = task.getSite();
            if (site.getDomain() == null && request.getUrl() != null) {
                site.setDomain(UrlUtils.getDomain(request.getUrl()));
            }
            return pushWhenNoDuplicate(task, request);
        }
        return null;
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
     * @param task    the task
     * @param request request
     */
    protected abstract CrawlResult pushWhenNoDuplicate(Task task, Request request);


    public int getTotalRequestsCount(Task task) {
        return duplicationProcessor.getTotalRequestsCount(task);
    }
}
