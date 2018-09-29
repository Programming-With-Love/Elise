package site.zido.elise.scheduler;

import site.zido.elise.Request;

public interface RequestManager {
    void pushRequest(Request request);

    Request nextRequest() throws InterruptedException;
}
