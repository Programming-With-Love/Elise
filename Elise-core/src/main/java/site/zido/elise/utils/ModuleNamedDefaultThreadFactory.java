package site.zido.elise.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * the user thread factory.
 *
 * @author zido
 */
public class ModuleNamedDefaultThreadFactory implements ThreadFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleNamedDefaultThreadFactory.class);
    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
    private final ThreadGroup group;
    private final String prefix;
    private volatile int threadNumber = 1;
    private boolean daemon;
    private String moduleName;

    /**
     * Instantiates a new Module named default thread factory.
     *
     * @param moduleName the module name
     */
    public ModuleNamedDefaultThreadFactory(String moduleName) {
        this(moduleName, false);
    }

    /**
     * Instantiates a new Module named default thread factory.
     *
     * @param moduleName the module name
     * @param daemon     the daemon
     */
    public ModuleNamedDefaultThreadFactory(String moduleName, boolean daemon) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        moduleName = moduleName.trim();
        prefix = moduleName + "-pool-" + POOL_NUMBER.getAndIncrement() + "-thread-";
        this.daemon = daemon;
        this.moduleName = moduleName;
    }

    @Override
    public Thread newThread(Runnable r) {
        StringBuilder sb = new StringBuilder();
        Thread t;
        synchronized (this) {
            int number = threadNumber;
            threadNumber++;
            t = new Thread(group, r, prefix + number, 0);
            sb.append("create a thread of ").append(moduleName);
        }
        t.setDaemon(daemon);
        sb.append(",daemon:").append(daemon);
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        LOGGER.debug(sb.toString());
        return t;
    }
}
