package com.hnqc.ironhand.spider.pipeline;

import com.hnqc.ironhand.spider.ResultItems;
import com.hnqc.ironhand.spider.Task;

public interface Pipeline {
    void process(ResultItems resultItems, Task task);
}
