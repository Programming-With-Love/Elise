package com.hnqc.ironhand.common.service.log;

import com.hnqc.ironhand.common.pojo.entity.IrLog;

/**
 * 任务执行纪录日志，写入数据库
 */
public interface IrLogService {
    void addLog(IrLog log);

}
