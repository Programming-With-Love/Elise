package com.hnqc.ironhand.common.pipelines;

import com.alibaba.fastjson.JSON;
import com.hnqc.ironhand.common.pojo.entity.ContentResult;
import com.hnqc.ironhand.common.service.IContentResultService;
import com.hnqc.ironhand.spider.ResultItem;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.pipeline.Pipeline;
import com.hnqc.ironhand.utils.IdWorker;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SavedPipeline implements Pipeline {
    private IContentResultService service;

    public SavedPipeline(IContentResultService service) {
        this.service = service;
    }

    @Override
    public void process(ResultItem resultItem, Task task) {
        Map<String, Object> all = resultItem.getAll();
        String json = JSON.toJSONString(all);
        ContentResult contentResult = new ContentResult();
        contentResult.setId(IdWorker.nextId());
        contentResult.setContent(json);
        contentResult.setSchedulerId(task.getId());
        service.addContentResult(contentResult);
    }
}
