package site.zido.elise.scheduler;

import site.zido.elise.http.impl.DefaultRequest;
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
 */
public class HashSetDeduplicationProcessor implements DuplicationProcessor {
    private Set<String> urls = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Override
    public boolean isDuplicate(Task task, DefaultRequest request) {
        return !urls.add(getUrl(request));
    }

    protected String getUrl(DefaultRequest request) {
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
