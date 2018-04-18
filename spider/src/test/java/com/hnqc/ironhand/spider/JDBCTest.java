package com.hnqc.ironhand.spider;

import com.hnqc.ironhand.spider.pipeline.MysqlPipeline;
import com.hnqc.ironhand.utils.IdWorker;
import org.junit.Test;

public class JDBCTest {
    @Test
    public void testInsert() {
        MysqlPipeline pipeline = new MysqlPipeline("renshi", new MysqlPipeline.IdGenerator() {
            @Override
            public Long getId() {
                return IdWorker.nextId();
            }
        });
        ResultItem resultItem = new ResultItem();
        resultItem.put("title","title");
        resultItem.put("content","dawdaefewgre");
        pipeline.process(resultItem,null);
    }
}
