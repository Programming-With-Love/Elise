package com.hnqc.ironhand.spider.pipeline;

import com.hnqc.ironhand.spider.ResultItems;
import com.hnqc.ironhand.spider.Task;

import java.util.Map;

public class ConsolePipeline implements Pipeline {
    @Override
    public void process(ResultItems resultItems, Task task) {
        System.out.println("get page: " + resultItems.getRequest().getUrl());

        Map<String, Object> all = resultItems.getAll();
        for (Map.Entry<String, Object> entry : all.entrySet()) {
            System.out.println(entry.getKey() + ":\t" + entry.getValue());
        }
    }
}
