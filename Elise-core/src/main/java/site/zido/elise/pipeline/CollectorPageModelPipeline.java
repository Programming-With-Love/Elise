package site.zido.elise.pipeline;

import java.util.List;

/**
 * 集合page model处理器
 *
 * @author zido
 */
public interface CollectorPageModelPipeline<T> extends PageModelPipeline<T> {

    /**
     * 获取集合
     *
     * @return 结果集合
     */
    List<T> getCollected();
}
