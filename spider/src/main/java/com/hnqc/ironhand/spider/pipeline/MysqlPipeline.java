package com.hnqc.ironhand.spider.pipeline;

import com.hnqc.ironhand.spider.ResultItems;
import com.hnqc.ironhand.spider.Task;
import com.hnqc.ironhand.spider.jdbc.JDBCHelper;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public class MysqlPipeline implements Pipeline {
    private String tableName;
    private IdGenerator generator;
    private static final Object LOCK = new Object();

    public interface IdGenerator {
        Long getId();
    }

    public MysqlPipeline(String tableName) {
        this.tableName = tableName;
    }

    public MysqlPipeline(String tableName, IdGenerator generator) {
        this.tableName = tableName;
        this.generator = generator;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        Map<String, Object> all = resultItems.getAll();
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
        synchronized (LOCK) {
            try {
                JDBCHelper.insertWithReturnPrimeKey(sql.toString(), objects);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
