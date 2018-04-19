package com.hnqc.ironhand.pipeline;

import com.hnqc.ironhand.ResultItem;
import com.hnqc.ironhand.Task;

import java.util.Map;
import java.util.Set;

/**
 * Abstract Sql Pipeline
 *
 * @author zido
 * @date 2018/04/20
 */
public abstract class AbstractSqlPipeline implements Pipeline {
    private String tableName;
    private IdGenerator generator;

    public interface IdGenerator {
        /**
         * get id
         *
         * @return id
         */
        Long getId();
    }

    public AbstractSqlPipeline(String tableName) {
        this.tableName = tableName;
    }

    public AbstractSqlPipeline(String tableName, IdGenerator generator) {
        this.tableName = tableName;
        this.generator = generator;
    }

    /**
     * the sql has been created
     *
     * @param sql    sql like 'insert into table (name,title) values(?,?)'
     * @param object some object
     */
    protected abstract void onInsert(String sql, Object[] object);

    @Override
    public void process(ResultItem resultItem, Task task) {
        Map<String, Object> all = resultItem.getAll();
        StringBuilder sql = new StringBuilder("insert into " + tableName + " (");
        if (generator != null) {
            sql.append("id");
        }
        Set<String> set = all.keySet();
        if (set.size() > 0) {
            sql.append(",").append(String.join(",", set));
        }
        sql.append(") values(");
        Object[] objects;
        int i = 0;
        if (generator != null) {
            objects = new Object[set.size() + 1];
            objects[i++] = generator.getId();
            sql.append("?,");
        } else {
            objects = new Object[set.size()];
        }
        for (String key : set) {
            sql.append("?,");
            objects[i++] = all.get(key);
        }
        sql = new StringBuilder(sql.substring(0, sql.length() - 1));
        sql.append(")");
        onInsert(sql.toString(), objects);
    }
}
