package com.hnqc.ironhand.spider.pipeline;

import com.hnqc.ironhand.spider.ResultItem;
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
     * @param resultItem 结果集
     * @param task        任务
     */
    void process(ResultItem resultItem, Task task);
}
