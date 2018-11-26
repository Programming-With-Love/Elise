package site.zido.elise.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.Task;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Default memory count manager.
 *
 * @author zido
 */
public class DefaultMemoryCountManager implements CountManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMemoryCountManager.class);
    private Map<Long, Integer> container = new HashMap<>();

    @Override
    public int count(Task task) {
        final Integer number = container.get(task.getId());
        if (number == null) {
            return 0;
        }
        return number;
    }

    @Override
    public synchronized void incr(Task task, int num, CountListener listener) {
        final int i = container.getOrDefault(task.getId(), 0) + num;
        container.put(task.getId(), i);
        LOGGER.debug("count result:" + i);
        if (listener != null) {
            listener.result(i);
        }
    }

}
