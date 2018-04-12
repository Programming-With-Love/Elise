package com.hnqc.ironhand.schedule.api;

import com.hnqc.ironhand.common.pojo.Seed;
import com.hnqc.ironhand.common.pojo.entity.Scheduler;
import com.hnqc.ironhand.common.sender.AnalyzerSender;
import com.hnqc.ironhand.schedule.pojo.AjaxResult;
import com.hnqc.ironhand.schedule.serivice.ITaskService;
import com.hnqc.ironhand.spider.distributed.configurable.ConfigurablePageProcessor;
import com.hnqc.ironhand.spider.distributed.DsSpiderImpl;
import com.hnqc.ironhand.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    private ITaskService taskService;


    private AnalyzerSender analyzerSender;

    /**
     * 创建spider接口
     *
     * @param pageProcessor 页面处理配置
     * @return 创建成功的id，前端可使用此id进行进度查询
     */
    @PostMapping
    public AjaxResult<String> createTask(@RequestBody ConfigurablePageProcessor pageProcessor) {
        long id = IdWorker.nextId();

        DsSpiderImpl spider = new DsSpiderImpl();
        spider.setId(id)
                .setStartTime(new Date())
                .setPageProcessor(pageProcessor);

        analyzerSender.send(new Seed(spider));

        return AjaxResult.<String>success().setData(String.valueOf(id));
    }

    @GetMapping("/{id}")
    public AjaxResult<Scheduler> getTask(@PathVariable Long id) {
        Scheduler scheduler = taskService.getTask(id);
        return AjaxResult.<Scheduler>success().setData(scheduler);
    }

    @Autowired
    public void setTaskService(ITaskService taskService) {
        this.taskService = taskService;
    }

    @Autowired
    public void setAnalyzerSender(AnalyzerSender analyzerSender) {
        this.analyzerSender = analyzerSender;
    }
}
