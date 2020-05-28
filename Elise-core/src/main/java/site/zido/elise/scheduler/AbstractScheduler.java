package site.zido.elise.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.Operator;
import site.zido.elise.Spider;
import site.zido.elise.custom.Config;
import site.zido.elise.custom.ConfigUtils;
import site.zido.elise.custom.GlobalConfig;
import site.zido.elise.downloader.DownloaderFactory;
import site.zido.elise.events.EventListener;
import site.zido.elise.events.TaskEventListener;
import site.zido.elise.http.Request;
import site.zido.elise.http.Response;
import site.zido.elise.http.impl.DefaultRequest;
import site.zido.elise.processor.ListenableResponseProcessor;
import site.zido.elise.processor.ResponseProcessor;
import site.zido.elise.select.SelectorMatchException;
import site.zido.elise.task.DefaultTask;
import site.zido.elise.task.Task;
import site.zido.elise.task.api.DefaultSelectableResponse;
import site.zido.elise.task.api.ResponseHandler;
import site.zido.elise.task.model.Model;
import site.zido.elise.utils.EventUtils;
import site.zido.elise.utils.IdWorker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 抽象调度程序，支持大多数生命周期方法
 *
 * @author zido
 */
public abstract class AbstractScheduler implements Spider, OperationalTaskScheduler {
    /**
     * 开始状态
     */
    private static final byte STATE_START = 0;
    /**
     * 暂停状态
     */
    private static final byte STATE_PAUSE = 1;
    /**
     * 取消状态
     */
    private static final byte STATE_CANCEL = 2;
    /**
     * 立即取消
     */
    private static final byte STATE_CANCEL_NOW = 3;
    /**
     * logger
     */
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
    /**
     * 状态容器
     */
    private final Map<Long, Byte> stateMap = new HashMap<>();
    /**
     * 暂停容器
     */
    private final Map<Long, Set<Seed>> pauseMap = new ConcurrentHashMap<>();
    /**
     * 监听器
     */
    private Set<EventListener> listeners = new HashSet<>();

    /**
     * 响应处理器
     */
    private ResponseProcessor responseProcessor;
    /**
     * 计数管理器
     */
    private CountManager countManager;
    /**
     * 去重处理器
     */
    private DuplicationProcessor duplicationProcessor;
    /**
     * 下载器工厂
     */
    private DownloaderFactory downloaderFactory;
    /**
     * 配置
     */
    private Config config;

    @Override
    public Operator of(ResponseHandler handler, Config config) {
        DefaultSelectableResponse response = new DefaultSelectableResponse();
        handler.onHandle(response);
        Model model = response.getModel();
        final DefaultTask task = new DefaultTask(IdWorker.nextId(), model, ConfigUtils.mergeConfig(config, this.config));
        return new DefaultOperator(task, this);
    }

    @Override
    public void pushRequest(Task task, Request request) {
        byte state = stateMap.computeIfAbsent(task.getId(), key -> STATE_START);
        //will no longer receive new requests when the task is in the canceled state
        if (state >= STATE_CANCEL) {
            return;
        }
        LOGGER.debug("get a candidate url {}", request.getUrl());
        if (shouldReserved(request)
            || noNeedToRemoveDuplicate(request)
            || !duplicationProcessor.isDuplicate(task, request)) {
            if (state == STATE_PAUSE) {
                LOGGER.debug(task.getId() + "[" + request.getUrl() + "] received pause");
                addToPauseMap(task.getId(), new Seed(task, request));
                countEvent(state, task);
                return;
            }
            LOGGER.debug("push to queue {}", request.getUrl());
            countManager.incr(task, 1);
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
        final Byte state = stateMap.get(task.getId());
        if (state == STATE_PAUSE) {
            addToPauseMap(task.getId(), new Seed(task, request));
            countEvent(state, task);
            return;
        }
        final Config config = task.getConfig();
        if (state != STATE_CANCEL_NOW) {
            try {
                Set<String> links = responseProcessor.process(task, response);
                //will no longer process any pages when the task is in the cancel_now state
                if (state != STATE_CANCEL) {
                    for (String link : links) {
                        pushRequest(task, new DefaultRequest(link));
                    }
                }
                countEvent(state, task);
                sleep(config.get(GlobalConfig.KEY_SLEEP_TIME));
                return;
            } catch (SelectorMatchException e) {
                e.printStackTrace();
            }
        }
        countEvent(state, task);
        if (state == STATE_START) {
            if (config.<Integer>get(GlobalConfig.KEY_RETRY_TIMES) == 0) {
                sleep(config.get(GlobalConfig.KEY_SLEEP_TIME));
            } else {
                // for cycle retry
                doCycleRetry(task, request);
            }
        }
    }

    private void countEvent(final byte state, Task task) {
        countManager.incr(task, -1, i -> {
            if (i == 0) {
                if (state == STATE_START) {
                    EventUtils.notifyListeners(listeners, listener -> {
                        if (listener instanceof TaskEventListener) {
                            ((TaskEventListener) listener).onSuccess(task);
                        }
                    });
                    //release downloader
                    downloaderFactory.release(task);
                } else if (state >= STATE_CANCEL) {
                    EventUtils.notifyListeners(listeners, listener -> {
                        if (listener instanceof TaskEventListener) {
                            ((TaskEventListener) listener).onCancel(task);
                        }
                    });
                    //release downloader
                    downloaderFactory.release(task);
                } else if (state == STATE_PAUSE) {
                    EventUtils.notifyListeners(listeners, listener -> {
                        if (listener instanceof TaskEventListener) {
                            ((TaskEventListener) listener).onPause(task);
                        }
                    });
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
        final Response response = downloaderFactory.create(task).download(task, request);
        if (response.isDownloadSuccess()) {
            EventUtils.mustNotifyListeners(listeners, listener -> {
                if (listener instanceof TaskEventListener) {
                    ((TaskEventListener) listener).onDownloadSuccess(task, request, response);
                }
            });
        } else {
            EventUtils.mustNotifyListeners(listeners, listener -> {
                if (listener instanceof TaskEventListener) {
                    ((TaskEventListener) listener).onDownloadError(task, request, response);
                }
            });
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
        Object cycleTriedTimesObject = request.getExtra(Request.CYCLE_TRIED_TIMES);
        if (cycleTriedTimesObject == null) {
            request.putExtra(Request.CYCLE_TRIED_TIMES, 1);
            pushRequest(task, request);
        } else {
            int cycleTriedTimes = (Integer) cycleTriedTimesObject;
            cycleTriedTimes++;
            if (cycleTriedTimes < task.getConfig().<Integer>get(GlobalConfig.KEY_SCHEDULE_RETRY_TIMES)) {
                request.putExtra(Request.CYCLE_TRIED_TIMES, cycleTriedTimes);
                pushRequest(task, request);
            }
        }
        sleep(task.getConfig().get(GlobalConfig.KEY_SCHEDULE_RETRY_TIMES));
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
        if (responseProcessor instanceof ListenableResponseProcessor) {
            ((ListenableResponseProcessor) responseProcessor).addEventListener(listener);
        }
    }

    @Override
    public void removeEventListener(EventListener listener) {
        listeners.remove(listener);
        if (responseProcessor instanceof ListenableResponseProcessor) {
            ((ListenableResponseProcessor) responseProcessor).removeEventListener(listener);
        }
    }

    @Override
    public boolean cancel(Task task, boolean giveUpSeeds) {
        final byte newState = giveUpSeeds ? STATE_CANCEL_NOW : STATE_CANCEL;
        synchronized (stateMap) {
            final Byte currentState = stateMap.get(task.getId());
            if (currentState != 0) {
                return currentState == newState;
            }
        }
        stateMap.put(task.getId(), newState);
        return true;
    }

    @Override
    public synchronized void pause(Task task) {
        final Byte currentState = stateMap.get(task.getId());
        if (currentState != STATE_START) {
            return;
        }
        stateMap.put(task.getId(), STATE_PAUSE);
    }

    @Override
    public synchronized boolean pause() {
        stateMap.replaceAll((key, value) -> {
            if (value == STATE_START) {
                return STATE_PAUSE;
            } else {
                return value;
            }
        });
        return true;
    }

    @Override
    public synchronized void recover() {
        stateMap.forEach((key, value) -> {
            if (value == 1) {
                recover(new DefaultTask(key, null, null));
            }
        });
    }

    @Override
    public synchronized void recover(Task task) {
        final Byte currentState = stateMap.get(task.getId());
        if (currentState == null) {
            return;
        }
        EventUtils.notifyListeners(listeners, eventListener -> {
            if (eventListener instanceof TaskEventListener) {
                ((TaskEventListener) eventListener).onRecover(task);
            }
        });
        stateMap.put(task.getId(), STATE_START);
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
     * Sets downloader factory.
     *
     * @param factory the factory
     */
    public void setDownloaderFactory(DownloaderFactory factory) {
        this.downloaderFactory = factory;
    }

    /**
     * Sets response handler.
     *
     * @param responseProcessor the response handler
     */
    public void setResponseProcessor(ResponseProcessor responseProcessor) {
        this.responseProcessor = responseProcessor;
    }

    /**
     * Sets count manager.
     *
     * @param countManager the count manager
     */
    public void setCountManager(CountManager countManager) {
        this.countManager = countManager;
    }

    /**
     * Sets duplication processor.
     *
     * @param duplicationProcessor the duplication processor
     */
    public void setDuplicationProcessor(DuplicationProcessor duplicationProcessor) {
        this.duplicationProcessor = duplicationProcessor;
    }

    /**
     * Sets config.
     *
     * @param config the config
     * @return the config
     */
    public AbstractScheduler setConfig(Config config) {
        this.config = config;
        return this;
    }
}
