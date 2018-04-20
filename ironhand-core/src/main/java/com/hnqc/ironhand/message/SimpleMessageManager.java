package com.hnqc.ironhand.message;

import com.hnqc.ironhand.scheduler.Seed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple Message Manager
 *
 * @author zido
 * @date 2018/04/20
 */
public class SimpleMessageManager extends ExecutorContainer implements MessageManager<String, Seed> {
    private static Logger logger = LoggerFactory.getLogger(SimpleMessageManager.class);
    protected AtomicInteger stat = new AtomicInteger(STAT_INIT);

    private final static String KEY_TYPE = "type";

    protected final static int STAT_INIT = 0;

    protected final static int STAT_RUNNING = 1;

    protected final static int STAT_STOPPED = 2;

    private final ThreadPoolExecutor rootExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r);
        }
    });
    /**
     * message queue
     */
    private final Queue<KeySeed> QUEUE = new ConcurrentLinkedQueue<>();

    static class KeySeed {
        String key;
        Seed seed;

        KeySeed(String key, Seed seed) {
            this.key = key;
            this.seed = seed;
        }
    }

    private final ThreadPoolExecutor childExecutor;

    public SimpleMessageManager(int poolSize) {
        this(new ThreadPoolExecutor(poolSize, poolSize, 0, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("thread child message manager");
                    return thread;
                }));
    }

    public SimpleMessageManager() {
        this(5);
    }

    public SimpleMessageManager(ThreadPoolExecutor childExecutor) {
        this.childExecutor = childExecutor;
    }

    public void setPoolSize(int poolSize) {
        childExecutor.setMaximumPoolSize(poolSize);
        childExecutor.setCorePoolSize(poolSize);
    }

    @Override
    public void send(String type, Seed message) {
        QUEUE.offer(new KeySeed(type, message));
    }

    private boolean checkRunningStat() {
        while (true) {
            int statNow = stat.get();
            if (statNow == STAT_RUNNING) {
                return true;
            }
            if (stat.compareAndSet(statNow, STAT_RUNNING)) {
                break;
            }
        }
        return false;
    }

    private void start() {
        if (checkRunningStat()) {
            return;
        }
        rootExecutor.execute(() -> {
            logger.debug("thread listening...");
            while (!Thread.currentThread().isInterrupted() && stat.get() == STAT_RUNNING) {
                KeySeed poll = QUEUE.poll();
                if (poll != null) {
                    Object target = super.getTargetByType(poll.key);
                    if (target == null) {
                        QUEUE.offer(poll);
                        continue;
                    }
                    MessageListener<Seed> listener = (MessageListener<Seed>) target;

                    childExecutor.execute(() -> listener.onListen(poll.seed));
                }
            }
        });
    }

    @Override
    public void listen(String type, MessageListener<Seed> listener) {
        super.register(type, listener);
        start();
    }

    public void stop() {
        if (stat.compareAndSet(STAT_RUNNING, STAT_STOPPED)) {
            logger.info("message listener stop success!");
        } else {
            logger.info("message listener stop fail!");
        }
    }

    public int blockSize() {
        return QUEUE.size();
    }

}
