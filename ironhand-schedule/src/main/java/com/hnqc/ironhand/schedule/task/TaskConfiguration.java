package com.hnqc.ironhand.schedule.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskConfiguration {
    @Bean
    public TaskListener listener(){
        return new TaskListener();
    }
}
