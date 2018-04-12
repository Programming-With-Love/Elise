package com.hnqc.ironhand.spider.pipeline;

import com.hnqc.ironhand.spider.ResultItems;
import com.hnqc.ironhand.spider.Task;

/**
 * 结果输出处理器
 *
 * @author zido
 * @date 2018/04/12
 */
public interface Pipeline {
    /**
     * 结果处理
     *
     * @param resultItems 结果集
     * @param task        任务
     */
    void process(ResultItems resultItems, Task task);
}
