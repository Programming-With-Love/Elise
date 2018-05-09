package site.zido.elise;

import site.zido.elise.pipeline.AbstractSqlPipeline;
import site.zido.elise.utils.IdWorker;
import org.junit.Test;

public class JDBCTest {
    @Test
    public void testInsert() {
        AbstractSqlPipeline pipeline = new AbstractSqlPipeline() {
            @Override
            public void onInsert(String sql, Object[] object) {
                System.out.println(sql);
                for (Object o : object) {
                    System.out.println(o);
                }
            }
        }.setGenerator(IdWorker::nextId);
        ResultItem resultItem = new ResultItem();
        resultItem.put("title", "title");
        resultItem.put("content", "dawdaefewgre");
        pipeline.process(resultItem, null);
    }
}
