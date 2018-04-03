package com.hnqc.ironhand.schedule.api;

import com.hnqc.ironhand.common.constants.Status;
import com.hnqc.ironhand.common.pojo.entity.Scheduler;
import com.hnqc.ironhand.schedule.pojo.AjaxResult;
import com.hnqc.ironhand.schedule.serivice.ITaskService;
import com.hnqc.ironhand.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    private ITaskService taskService;

    @PostMapping("/create")
    public AjaxResult<String> createTask(@RequestBody Scheduler scheduler) {
        long id = IdWorker.nextId();
        scheduler.setScheduleId(id);
        scheduler.setStatus(Status.RUNNING);
        scheduler.setCreateTime(new Date());
        taskService.addTask(scheduler);
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
}
