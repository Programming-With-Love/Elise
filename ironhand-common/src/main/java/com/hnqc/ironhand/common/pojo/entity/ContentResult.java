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
    private Long schedulerId;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    public ContentResult() {
    }

    public ContentResult(Long schedulerId) {
        this.schedulerId = schedulerId;
        this.id = IdWorker.nextId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(Long schedulerId) {
        this.schedulerId = schedulerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
