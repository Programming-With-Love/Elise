package com.hnqc.ironhand.schedule.serivice.impl;

import com.hnqc.ironhand.common.pojo.entity.Scheduler;
import com.hnqc.ironhand.common.repository.SchedulerRepository;
import com.hnqc.ironhand.schedule.serivice.ITaskService;
import com.hnqc.ironhand.spider.distributed.DsSpiderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskServiceImpl implements ITaskService {
    private SchedulerRepository schedulerRepository;

    @Override
    public void addTask(Scheduler scheduler) {
        schedulerRepository.save(scheduler);
    }

    @Override
    public Scheduler getTask(Long id) {
        Optional<Scheduler> opt = schedulerRepository.findById(id);
        return opt.orElse(null);
    }

    @Autowired
    public void setSchedulerRepository(SchedulerRepository schedulerRepository) {
        this.schedulerRepository = schedulerRepository;
    }
}
