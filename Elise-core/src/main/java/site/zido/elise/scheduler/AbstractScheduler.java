package site.zido.elise.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.*;
import site.zido.elise.downloader.Downloader;
import site.zido.elise.processor.ListenablePageProcessor;
import site.zido.elise.processor.PageProcessor;
import site.zido.elise.select.CompilerException;
import site.zido.elise.select.NumberExpressMatcher;
import site.zido.elise.utils.EventUtils;
import site.zido.elise.utils.UrlUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract Duplicate Removed Scheduler
 *
 * @author zido
 */
public abstract class AbstractScheduler implements TaskScheduler {
    protected Logger LOGGER = LoggerFactory.getLogger(getClass());

    private Set<EventListener> listeners = new HashSet<>();

    @Override
    public void pushRequest(Task task, Request request) {
        LOGGER.debug("get a candidate url {}", request.getUrl());
        if (shouldReserved(request)
                || noNeedToRemoveDuplicate(request)
                || !getDuplicationProcessor().isDuplicate(task, request)) {
            LOGGER.debug("push to queue {}", request.getUrl());
            getCountManager().incr(task, 1);
            Site site = task.getSite();
            if (site.getDomain() == null && request.getUrl() != null) {
                site.setDomain(UrlUtils.getDomain(request.getUrl()));
            }
            pushWhenNoDuplicate(task, request);
        }
    }

    protected void onProcess(Task task, Request request, Page page) {
        if (page.isDownloadSuccess()) {
            Site site = task.getSite();
            String codeAccepter = site.getCodeAccepter();
            NumberExpressMatcher matcher;
            try {
                matcher = new NumberExpressMatcher(codeAccepter);
            } catch (CompilerException e) {
                throw new RuntimeException(e);
            }
            if (matcher.matches(page.getStatusCode())) {
                Set<String> links = getProcessor().process(task, page);
                for (String link : links) {
                    pushRequest(task, new Request(link));
                }
                getCountManager().incr(task, -1, i -> {
                    if (i == 0) {
                        EventUtils.notifyListeners(listeners, listener -> listener.onSuccess(task));
                    }
                });
                sleep(site.getSleepTime());
                return;
            }
        }
        getCountManager().incr(task, -1, i -> {
            if (i == 0) {
                EventUtils.notifyListeners(listeners, listener -> listener.onSuccess(task));
            }
        });
        Site site = task.getSite();
        if (site.getCycleRetryTimes() == 0) {
            sleep(site.getSleepTime());
        } else {
            // for cycle retry
            doCycleRetry(task, request);
        }
    }

    protected Page onDownload(Task task, Request request) {
        final Page page = getDownloader().download(task, request);

        if (page.isDownloadSuccess()) {
            EventUtils.mustNotifyListeners(listeners, listener -> listener.onDownloadSuccess(task, request, page));
        } else {
            EventUtils.mustNotifyListeners(listeners, listener -> listener.onDownloadError(task, request, page));
        }
        return page;
    }

    protected void onCancel() {
        EventUtils.mustNotifyListeners(listeners, EventListener::onCancel);
    }

    private void doCycleRetry(Task task, Request request) {
        Object cycleTriedTimesObject = request.getExtra(Request.CYCLE_TRIED_TIMES);
        if (cycleTriedTimesObject == null) {
            pushRequest(task, new Request(request).putExtra(Request.CYCLE_TRIED_TIMES, 1));
        } else {
            int cycleTriedTimes = (Integer) cycleTriedTimesObject;
            cycleTriedTimes++;
            if (cycleTriedTimes < task.getSite().getCycleRetryTimes()) {
                pushRequest(task, new Request(request).putExtra(Request.CYCLE_TRIED_TIMES, cycleTriedTimes));
            }
        }
        sleep(task.getSite().getRetrySleepTime());
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            LOGGER.error("Thread interrupted when sleep", e);
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
     * @param task    the task
     * @param request request
     */
    protected abstract void pushWhenNoDuplicate(Task task, Request request);

    @Override
    public void addEventListener(EventListener listener) {
        listeners.add(listener);
        if (getProcessor() instanceof ListenablePageProcessor) {
            ((ListenablePageProcessor) getProcessor()).addEventListener(listener);
        }
    }

    public abstract Downloader getDownloader();

    public abstract PageProcessor getProcessor();

    public abstract CountManager getCountManager();

    public abstract DuplicationProcessor getDuplicationProcessor();
}
