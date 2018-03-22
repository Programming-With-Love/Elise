package com.hnqc.ironhand.common.pojo.entity;

import com.alibaba.fastjson.JSON;
import com.hnqc.ironhand.common.constants.ScheduleMode;
import com.hnqc.ironhand.common.constants.Status;
import com.hnqc.ironhand.common.query.SchedulerBuilder;
import com.hnqc.ironhand.common.query.UrlQuery;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Scheduler {
    /**
     * id
     */
    @Id
    private Long scheduleId;
    /**
     * 解析规则,数组index为深度
     */
    private transient List<UrlQuery> urlQueries;

    /**
     * 入口url <strong>数组</strong> json字符串
     */
    @Column(length = 2000)
    private String entries;

    /**
     * 当前状态
     */
    private Status status;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    //定时执行模式
    private ScheduleMode mode;

    //执行时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date runTime;

    public static SchedulerBuilder builder() {
        return new SchedulerBuilder();
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public List<UrlQuery> getUrlQueries() {
        return urlQueries;
    }

    public void setUrlQueries(List<UrlQuery> urlQueries) {
        this.urlQueries = urlQueries;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public ScheduleMode getMode() {
        return mode;
    }

    public void setMode(ScheduleMode mode) {
        this.mode = mode;
    }

    public Date getRunTime() {
        return runTime;
    }

    public void setRunTime(Date runTime) {
        this.runTime = runTime;
    }

    public String getEntries() {
        return entries;
    }

    public void setEntries(String entries) {
        this.entries = entries;
    }

    @Column(length = 10000)
    public String getJsonUrlQueries() {
        return JSON.toJSONString(this.getUrlQueries());
    }

    public void setJsonUrlQueries(String jsonUrlQueries) {
        this.setUrlQueries(JSON.parseArray(jsonUrlQueries, UrlQuery.class));
    }
}
