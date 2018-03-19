package com.hnqc.ironhand.schedule.serivice.impl;

import com.hnqc.ironhand.common.pojo.entity.Task;
import com.hnqc.ironhand.common.repository.TaskRepository;
import com.hnqc.ironhand.schedule.serivice.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskServiceImpl implements ITaskService {
    private TaskRepository taskRepository;

    @Override
    public void addTask(Task task) {
        taskRepository.save(task);
    }

    @Override
    public Task getTask(Long id) {
        Optional<Task> opt = taskRepository.findById(id);
        return opt.orElse(null);
    }

    @Autowired
    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
}
