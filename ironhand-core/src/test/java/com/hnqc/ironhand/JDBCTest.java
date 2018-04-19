package com.hnqc.ironhand;

import com.hnqc.ironhand.pipeline.AbstractSqlPipeline;
import com.hnqc.ironhand.utils.IdWorker;
import org.junit.Test;

public class JDBCTest {
    @Test
    public void testInsert() {
        AbstractSqlPipeline pipeline = new AbstractSqlPipeline("renshi", IdWorker::nextId){
            @Override
            protected void onInsert(String sql, Object[] object) {
                System.out.println(sql);
                for (Object o : object) {
                    System.out.println(o);
                }
            }
        };
        ResultItem resultItem = new ResultItem();
        resultItem.put("title","title");
        resultItem.put("content","dawdaefewgre");
        pipeline.process(resultItem,null);
    }
}
