package com.hnqc.ironhand.common.pojo.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hnqc.ironhand.common.pojo.SeedData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

@Entity
public class Seed {
    @Transient
    private ObjectMapper objectMapper = new ObjectMapper();
    private static Logger logger = LoggerFactory.getLogger(Seed.class);
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    @Column(name = "data", length = 3000)
    private String jsonData;
    @Transient
    private SeedData data;
    private Integer taskId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public SeedData getData() {
        return data;
    }

    public void setData(SeedData data) {
        this.data = data;
        try {
            this.jsonData = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            logger.error(String.format("seed[id:%d]设置json失败", id), e);

        }
    }
}
