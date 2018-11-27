package site.zido.elise.scheduler;

import site.zido.elise.Task;
import site.zido.elise.http.Request;

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
    public boolean isDuplicate(Task task, Request request) {
        return !urls.add(getUrl(request));
    }

    /**
     * Gets url.
     *
     * @param request the request
     * @return the url
     */
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
