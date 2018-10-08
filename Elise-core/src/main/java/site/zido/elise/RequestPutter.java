package site.zido.elise;

import java.util.concurrent.Future;

public interface RequestPutter {
    Future<CrawlResult> pushRequest(Task task, Request request);
}
