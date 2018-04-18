package com.hnqc.ironhand.schedule.api;

import com.hnqc.ironhand.common.pojo.entity.Scheduler;
import com.hnqc.ironhand.common.sender.AnalyzerSender;
import com.hnqc.ironhand.schedule.pojo.AjaxResult;
import com.hnqc.ironhand.schedule.serivice.ITaskService;
import com.hnqc.ironhand.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    private ITaskService taskService;


    private AnalyzerSender analyzerSender;

    /**
     * 创建spider接口
     *
     * @return 创建成功的id，前端可使用此id进行进度查询
     */
    @PostMapping
    public AjaxResult<String> createTask() {
        long id = IdWorker.nextId();

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
