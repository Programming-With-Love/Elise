package com.hnqc.ironhand.common.pojo.entity;

import com.hnqc.ironhand.common.constants.ScheduleMode;

import javax.persistence.*;
import java.util.Date;

/**
 * 总任务
 */
@Entity
public class Scheduler {
    @Id
    @GeneratedValue
    private Long id;


    //定时执行模式
    private ScheduleMode mode;

    //执行时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date runTime;

    //创建实践
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;


}
