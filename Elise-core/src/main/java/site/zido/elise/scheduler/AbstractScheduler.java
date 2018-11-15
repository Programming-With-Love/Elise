package site.zido.elise.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.*;
import site.zido.elise.downloader.Downloader;
import site.zido.elise.processor.PageProcessor;
import site.zido.elise.select.CompilerException;
import site.zido.elise.select.NumberExpressMatcher;
import site.zido.elise.utils.UrlUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Abstract Duplicate Removed Scheduler
 *
 * @author zido
 */
public abstract class AbstractScheduler implements TaskScheduler {
    protected Logger LOGGER = LoggerFactory.getLogger(getClass());

    private DuplicationProcessor duplicationProcessor;
    private Downloader downloader;
    private PageProcessor processor;
    private Set<EventListener> listeners = new HashSet<>();


    public AbstractScheduler(DuplicationProcessor duplicationProcessor) {
        this.duplicationProcessor = duplicationProcessor;
    }

    @Override
    public void pushRequest(Task task, Request request) {
        LOGGER.debug("get a candidate url {}", request.getUrl());
        if (shouldReserved(request)
                || noNeedToRemoveDuplicate(request)
                || !duplicationProcessor.isDuplicate(task, request)) {
            LOGGER.debug("push to queue {}", request.getUrl());
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
                List<String> links = processor.process(task, page);
                for (String link : links) {
                    pushRequest(task, new Request(link));
                }
                sleep(site.getSleepTime());
                return;
            }
        }
        Site site = task.getSite();
        if (site.getCycleRetryTimes() == 0) {
            sleep(site.getSleepTime());
        } else {
            // for cycle retry
            doCycleRetry(task, request);
        }
    }

    protected Page onDownload(Task task, Request request) {
        final Page page = downloader.download(task, request);
        if (page.isDownloadSuccess()) {
            notifyListeners(listener -> {
                listener.onDownloadSuccess(task, request, page);
            });
        } else {
            notifyListeners((listener) -> {
                listener.onDownloadError(task, request, page);
            });
        }
        return page;
    }

    protected void notifyListeners(Consumer<EventListener> callback) {
        for (EventListener listener : listeners) {
            try {
                callback.accept(listener);
            } catch (Throwable t) {
                LOGGER.error("listen error", t);
            }
        }
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
    }

    public int getTotalRequestsCount(Task task) {
        return duplicationProcessor.getTotalRequestsCount(task);
    }

    public Downloader getDownloader() {
        return this.downloader;
    }

    public PageProcessor getProcessor() {
        return this.processor;
    }

    public void setDownloader(Downloader downloader) {
        this.downloader = downloader;
    }

    public void setProcessor(PageProcessor processor) {
        this.processor = processor;
    }
}
