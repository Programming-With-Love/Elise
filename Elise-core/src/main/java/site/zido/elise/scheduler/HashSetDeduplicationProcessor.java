package site.zido.elise.scheduler;

import site.zido.elise.Request;
import site.zido.elise.Task;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Remove duplicate task processor using hash set.
 * <p>
 * It is process isolation and thread-safe{@link ConcurrentHashMap}
 *
 * @author zido
 * @date 2018/04/20
 */
public class HashSetDeduplicationProcessor implements DuplicationProcessor {
    private Set<String> urls = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Override
    public boolean isDuplicate(Request request, Task task) {
        return !urls.add(getUrl(request));
    }

    protected String getUrl(Request request) {
        return request.getUrl();
    }

    @Override
    public void resetDuplicateCheck(Task task) {
        urls.clear();
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        return urls.size();
    }
}
