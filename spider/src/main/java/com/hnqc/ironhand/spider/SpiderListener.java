package com.hnqc.ironhand.spider;

/**
 * com.hnqc.ironhand.spider
 *
 * @author zido
 */
public interface SpiderListener {
    void onSuccess(Request request);

    void onError(Request request);
}
