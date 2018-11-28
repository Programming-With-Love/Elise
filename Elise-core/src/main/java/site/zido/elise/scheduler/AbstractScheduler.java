package site.zido.elise.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.EventListener;
import site.zido.elise.Task;
import site.zido.elise.custom.Config;
import site.zido.elise.custom.GlobalConfig;
import site.zido.elise.downloader.Downloader;
import site.zido.elise.http.Request;
import site.zido.elise.http.Response;
import site.zido.elise.http.impl.DefaultRequest;
import site.zido.elise.processor.ListenableResponseHandler;
import site.zido.elise.processor.ResponseHandler;
import site.zido.elise.select.matcher.CompilerException;
import site.zido.elise.select.matcher.NumberExpressMatcher;
import site.zido.elise.utils.EventUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract Duplicate Removed Scheduler
 *
 * @author zido
 */
public abstract class AbstractScheduler implements TaskScheduler {
    private static final byte STATE_PAUSE = 1;
    private static final byte STATE_CANCEL = 2;
    private static final byte STATE_CANCEL_NOW = 3;
    /**
     * The Logger.
     */
    protected static Logger LOGGER = LoggerFactory.getLogger(TaskScheduler.class);
    private final Map<Long, Byte> stateMap = new HashMap<>();
    private final Map<Long, Set<Seed>> pauseMap = new ConcurrentHashMap<>();
    private Set<EventListener> listeners = new HashSet<>();

    @Override
    public void pushRequest(Task task, Request request) {
        Byte state = stateMap.getOrDefault(task.getId(), (byte) -1);
        //will no longer receive new requests when the task is in the canceled state
        if (state >= STATE_CANCEL) {
            return;
        }
        LOGGER.debug("get a candidate url {}", request.getUrl());
        if (shouldReserved(request)
                || noNeedToRemoveDuplicate(request)
                || !getDuplicationProcessor().isDuplicate(task, request)) {
            if (state == STATE_PAUSE) {
                LOGGER.debug(task.getId() + "[" + request.getUrl() + "] received pause");
                addToPauseMap(task.getId(), new Seed(task, request));
                countEvent(state, task);
                return;
            }
            LOGGER.debug("push to queue {}", request.getUrl());
            getCountManager().incr(task, 1);
            pushWhenNoDuplicate(task, request);
        }
    }

    private void addToPauseMap(Long taskId, Seed seed) {
        Set<Seed> seeds = pauseMap.computeIfAbsent(taskId, k -> new HashSet<>());
        seeds.add(seed);
    }

    /**
     * On process.
     *
     * @param task     the task
     * @param request  the request
     * @param response the response
     */
    protected void onProcess(Task task, Request request, Response response) {
        //will no longer process any pages when the task is in the cancel_now state
        final Byte state = stateMap.getOrDefault(task.getId(), (byte) -1);
        if (state == STATE_PAUSE) {
            addToPauseMap(task.getId(), new Seed(task, request));
            countEvent(state, task);
            return;
        }
        final Config config = task.modelExtractor().getConfig();
        if (state != STATE_CANCEL_NOW) {
            String successCode = config.get(GlobalConfig.KEY_SUCCESS_CODE);
            NumberExpressMatcher matcher;
            try {
                matcher = new NumberExpressMatcher(successCode);
            } catch (CompilerException e) {
                throw new RuntimeException(e);
            }
            if (matcher.matches(response.getStatusCode())) {
                Set<String> links = getResponsehandler().process(task, response);
                //will no longer process any pages when the task is in the cancel_now state
                if (state != STATE_CANCEL) {
                    for (String link : links) {
                        pushRequest(task, new DefaultRequest(link));
                    }
                }
                countEvent(state, task);
                sleep(config.get(config.get(GlobalConfig.KEY_SLEEP_TIME)));
                return;
            }
        }
        countEvent(state, task);
        if (state == -1) {
            if (config.<Integer>get(GlobalConfig.KEY_RETRY_TIMES) == 0) {
                sleep(config.get(GlobalConfig.KEY_SLEEP_TIME));
            } else {
                // for cycle retry
                doCycleRetry(task, request);
            }
        }
    }

    private void countEvent(final byte state, Task task) {
        getCountManager().incr(task, -1, i -> {
            if (i == 0) {
                if (state == -1) {
                    EventUtils.notifyListeners(listeners, listener -> listener.onSuccess(task));
                } else if (state > STATE_CANCEL) {
                    EventUtils.notifyListeners(listeners, listener -> listener.onCancel(task));
                } else if (state == STATE_PAUSE) {
                    EventUtils.notifyListeners(listeners, listeners -> listeners.onPause(task));
                }
            }
        });
    }

    /**
     * On download default response.
     *
     * @param task    the task
     * @param request the request
     * @return the default response
     */
    protected Response onDownload(Task task, Request request) {
        final Response response = getDownloader().download(task, request);

        if (response.isDownloadSuccess()) {
            EventUtils.mustNotifyListeners(listeners, listener -> listener.onDownloadSuccess(task, request, response));
        } else {
            EventUtils.mustNotifyListeners(listeners, listener -> listener.onDownloadError(task, request, response));
        }
        return response;
    }

    /**
     * On cancel.
     */
    protected void onCancel() {
        EventUtils.mustNotifyListeners(listeners, EventListener::onCancel);
    }

    private void doCycleRetry(Task task, Request request) {
        Object cycleTriedTimesObject = request.getExtra(DefaultRequest.CYCLE_TRIED_TIMES);
        if (cycleTriedTimesObject == null) {
            pushRequest(task, new DefaultRequest(request).putExtra(DefaultRequest.CYCLE_TRIED_TIMES, 1));
        } else {
            int cycleTriedTimes = (Integer) cycleTriedTimesObject;
            cycleTriedTimes++;
            if (cycleTriedTimes < task.getSite().getCycleRetryTimes()) {
                pushRequest(task, new DefaultRequest(request).putExtra(DefaultRequest.CYCLE_TRIED_TIMES, cycleTriedTimes));
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
        return request.getExtra(DefaultRequest.CYCLE_TRIED_TIMES) != null;
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
        if (getResponsehandler() instanceof ListenableResponseHandler) {
            ((ListenableResponseHandler) getResponsehandler()).addEventListener(listener);
        }
    }

    @Override
    public boolean cancel(Task task, boolean giveUpSeeds) {
        final byte newState = giveUpSeeds ? STATE_CANCEL_NOW : STATE_CANCEL;
        synchronized (stateMap) {
            final Byte currentState = stateMap.get(task.getId());
            if (currentState != null) {
                return currentState == newState;
            }
        }
        stateMap.put(task.getId(), newState);
        return true;
    }

    @Override
    public synchronized boolean pause(Task task) {
        final Byte currentState = stateMap.get(task.getId());
        if (currentState != null) {
            return currentState == STATE_PAUSE;
        }
        stateMap.put(task.getId(), STATE_PAUSE);
        return true;
    }

    @Override
    public synchronized void recover(Task task) {
        final Byte currentState = stateMap.get(task.getId());
        if (currentState == null) {
            return;
        }
        EventUtils.notifyListeners(listeners, eventListener -> eventListener.onRecover(task));
        stateMap.remove(task.getId());
        Set<Seed> set = pauseMap.getOrDefault(task.getId(), new HashSet<>());
        for (Seed seed : set) {
            if (seed.getResponse() != null) {
                onProcess(seed.getTask(), seed.getRequest(), seed.getResponse());
            } else {
                pushWhenNoDuplicate(seed.getTask(), seed.getRequest());
            }
        }
    }

    /**
     * Gets downloader.
     *
     * @return the downloader
     */
    public abstract Downloader getDownloader();

    /**
     * Gets response handler.
     *
     * @return the response handler
     */
    public abstract ResponseHandler getResponsehandler();

    /**
     * Gets count manager.
     *
     * @return the count manager
     */
    public abstract CountManager getCountManager();

    /**
     * Gets duplication processor.
     *
     * @return the duplication processor
     */
    public abstract DuplicationProcessor getDuplicationProcessor();
}
