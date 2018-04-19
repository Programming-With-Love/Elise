package com.hnqc.ironhand.analyzer;

import com.hnqc.ironhand.common.repository.SchedulerRepository;
import com.hnqc.ironhand.common.sender.DownLoadSender;
import com.hnqc.ironhand.common.service.IFileService;
import com.hnqc.ironhand.spider.Spider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Analyzer {
    private SchedulerRepository schedulerRepository;
    private IFileService fileService;
    private DownLoadSender downLoadSender;
    private Logger logger = LoggerFactory.getLogger(Analyzer.class);

    @Autowired
    public void setSchedulerRepository(SchedulerRepository schedulerRepository) {
        this.schedulerRepository = schedulerRepository;
    }

    @Autowired
    public void setFileService(IFileService fileService) {
        this.fileService = fileService;
    }

    @Autowired
    public void setDownLoadSender(DownLoadSender downLoadSender) {
        this.downLoadSender = downLoadSender;
    }
}
