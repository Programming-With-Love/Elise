package com.hnqc.ironhand.common.pojo.entity;

import com.hnqc.ironhand.utils.IdWorker;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * 结果实体类
 */
@Entity
public class ContentResult {
    private Long id;
    private Long SchedulerId;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    public ContentResult() {
    }

    public ContentResult(Long SchedulerId) {
        this.SchedulerId = SchedulerId;
        this.id = IdWorker.nextId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSchedulerId() {
        return SchedulerId;
    }

    public void setSchedulerId(Long schedulerId) {
        this.SchedulerId = schedulerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
