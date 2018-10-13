package site.zido.elise.utils;

import site.zido.elise.thread.ModuleNamedDefaultThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * High concurrency performance significantly improves performance (space Exchange time) through memory cache current time.
 *
 * @author zido
 */
public class SystemClock {
    private final long period;
    private final AtomicLong now;

    private SystemClock(long period) {
        this.period = period;
        this.now = new AtomicLong(System.currentTimeMillis());
        scheduleClockUpdating();
    }

    private static SystemClock instance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * Now long.
     *
     * @return the long
     */
    public static long now() {
        return instance().currentTimeMillis();
    }

    private void scheduleClockUpdating() {
        ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1, new ModuleNamedDefaultThreadFactory("system-clock", true));
        scheduler.scheduleAtFixedRate(() -> now.set(System.currentTimeMillis()), period, period, TimeUnit.MILLISECONDS);
    }

    private long currentTimeMillis() {
        return now.get();
    }

    private static class InstanceHolder {
        /**
         * The Instance.
         */
        static final SystemClock INSTANCE = new SystemClock(1);
    }

}
