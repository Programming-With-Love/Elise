package com.hnqc.ironhand.common.service;

import com.hnqc.ironhand.common.pojo.entity.ContentResult;

public interface IContentResultService {
    /**
     * 添加内容结果，会增加统计信息
     *
     * @param result 结果
     */
    void addContentResult(ContentResult result);
}
