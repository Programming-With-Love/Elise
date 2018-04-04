package com.hnqc.ironhand.common.service.impl;

import com.hnqc.ironhand.common.pojo.entity.ContentResult;
import com.hnqc.ironhand.common.pojo.entity.SchedulerCount;
import com.hnqc.ironhand.common.repository.ContentResultRepository;
import com.hnqc.ironhand.common.repository.SchedulerCountRepository;
import com.hnqc.ironhand.common.service.IContentResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
public class ContentResultServiceImpl implements IContentResultService {
    private ContentResultRepository contentResultRepository;
    private SchedulerCountRepository schedulerCountRepository;

    @Transactional
    public void addContentResult(ContentResult result) {
        Long schedulerId = result.getSchedulerId();
        if (schedulerId == null) {
            return;
        }
        contentResultRepository.save(result);
        Optional<SchedulerCount> optionalCount = schedulerCountRepository.findById(schedulerId);
        optionalCount.orElseGet(() -> {
            SchedulerCount tmpCount = new SchedulerCount();
            tmpCount.setStartTime(new Date());
            tmpCount.setSchedulerId(schedulerId);
            tmpCount.setCount(0L);
            schedulerCountRepository.save(tmpCount);
            return tmpCount;
        });
        schedulerCountRepository.increaseCount(schedulerId);
    }

    @Autowired
    public void setContentResultRepository(ContentResultRepository resultRepository) {
        this.contentResultRepository = resultRepository;
    }


    @Autowired
    public void setSchedulerCountRepository(SchedulerCountRepository repository) {
        this.schedulerCountRepository = repository;
    }
}
