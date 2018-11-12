package site.zido.elise;

import site.zido.elise.processor.CrawlResult;

public interface RequestPutter {
    CrawlResult pushRequest(Task task, Request request);
}
