package com.hnqc.ironhand.schedule.api;

import com.hnqc.ironhand.common.constants.Status;
import com.hnqc.ironhand.common.pojo.entity.Task;
import com.hnqc.ironhand.common.utils.IdWorker;
import com.hnqc.ironhand.schedule.pojo.AjaxResult;
import com.hnqc.ironhand.schedule.serivice.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    private ITaskService taskService;

    @PostMapping("/create")
    public AjaxResult<String> createTask(@RequestBody Task task) {
        long id = IdWorker.nextId();
        task.setScheduleId(id);
        task.setStatus(Status.RUNNING);
        task.setCreateTime(new Date());
        taskService.addTask(task);
        return AjaxResult.<String>success().setData(String.valueOf(id));
    }

    @GetMapping("/{id}")
    public AjaxResult<Task> getTask(@PathVariable Long id) {
        Task task = taskService.getTask(id);
        return AjaxResult.<Task>success().setData(task);
    }

    @Autowired
    public void setTaskService(ITaskService taskService) {
        this.taskService = taskService;
    }
}
