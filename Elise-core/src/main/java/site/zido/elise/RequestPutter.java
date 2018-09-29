package site.zido.elise;

import java.util.concurrent.Future;

public interface RequestPutter {
    Future<ResultItem> pushRequest(Task task,Request request);
}
