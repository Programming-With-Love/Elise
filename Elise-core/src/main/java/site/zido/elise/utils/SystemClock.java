package site.zido.elise.utils;

import java.sql.Timestamp;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 通过内存缓存当前时间，高并发情况下性能显著提升性能（空间换取时间）。
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

    private static class InstanceHolder {
        /**
         * The Instance.
         */
        static final SystemClock INSTANCE = new SystemClock(1);
    }

    private static SystemClock instance() {
        return InstanceHolder.INSTANCE;
    }

    private void scheduleClockUpdating() {

        ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1,
                r -> {
                    Thread thread = new Thread("System Clock");
                    thread.setDaemon(true);
                    return thread;
                });
        scheduler.scheduleAtFixedRate(() -> now.set(System.currentTimeMillis()), period, period, TimeUnit.MILLISECONDS);
    }

    private long currentTimeMillis() {
        return now.get();
    }

    /**
     * Now long.
     *
     * @return the long
     */
    public static long now() {
        return instance().currentTimeMillis();
    }

}
