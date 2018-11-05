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
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private volatile int threadNumber = 1;
    private final ThreadGroup group;
    //will be a part of name
    private final String prefix;
    private boolean daemon;
    private String moduleName;

    public ModuleNamedDefaultThreadFactory(String moduleName) {
        this(moduleName, false);
    }

    public ModuleNamedDefaultThreadFactory(String moduleName, boolean daemon) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        prefix = moduleName + "-pool-" + poolNumber.getAndIncrement() + "-thread-";
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
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        LOGGER.debug(sb.toString());
        return t;
    }
}
