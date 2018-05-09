package site.zido.elise.pipeline;

import java.util.List;

/**
 * 集合page model处理器
 *
 * @author zido
 * @date 2018/44/12
 */
public interface CollectorPageModelPipeline<T> extends PageModelPipeline<T> {

    /**
     * 获取集合
     *
     * @return 结果集合
     */
    List<T> getCollected();
}
