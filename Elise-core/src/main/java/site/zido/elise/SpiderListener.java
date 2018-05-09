package site.zido.elise;

/**
 * site.zido.elise.spider
 *
 * @author zido
 */
public interface SpiderListener {
    void onSuccess(Request request);

    void onError(Request request);
}
