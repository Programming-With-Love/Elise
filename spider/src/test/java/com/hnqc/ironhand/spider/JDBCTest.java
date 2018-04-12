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
        ResultItems resultItems = new ResultItems();
        resultItems.put("title","title");
        resultItems.put("content","dawdaefewgre");
        pipeline.process(resultItems,null);
    }
}
