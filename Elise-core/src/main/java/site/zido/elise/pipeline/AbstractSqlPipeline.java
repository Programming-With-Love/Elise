package site.zido.elise.pipeline;

import site.zido.elise.DistributedTask;
import site.zido.elise.ResultItem;
import site.zido.elise.Task;
import site.zido.elise.configurable.DefRootExtractor;
import site.zido.elise.utils.ValidateUtils;

import java.util.Map;
import java.util.Set;

/**
 * Abstract Sql Pipeline
 *
 * @author zido
 * @date 2018/04/20
 */
public abstract class AbstractSqlPipeline implements Pipeline {
    private String defaultTableName = "elise_result";
    private IdGenerator generator;
    private Table table = new Table() {
    };

    public interface IdGenerator {
        /**
         * get id
         *
         * @return id
         */
        Long getId();
    }

    interface Table {
        /**
         * 获取表名
         *
         * @param task task
         * @return 表名
         */
        default String getTableName(Task task) {
            if (task != null && task instanceof DistributedTask) {
                DefRootExtractor defExtractor = ((DistributedTask) task).getDefExtractor();
                return defExtractor.getName();
            }
            return null;
        }
    }

    public AbstractSqlPipeline() {
    }

    public AbstractSqlPipeline(Table table, IdGenerator generator) {
        this.generator = generator;
        this.table = table;
    }

    public AbstractSqlPipeline setDefaultTableName(String defaultTableName) {
        this.defaultTableName = defaultTableName;
        return this;
    }

    public AbstractSqlPipeline setGenerator(IdGenerator generator) {
        this.generator = generator;
        return this;
    }

    public AbstractSqlPipeline setTable(Table table) {
        this.table = table;
        return this;
    }

    /**
     * the sql has been created
     *
     * @param sql    sql like 'insert into table (name,title) values(?,?)'
     * @param object some object
     */
    public abstract void onInsert(String sql, Object[] object);

    @Override
    public void process(ResultItem resultItem, Task task) {
        Map<String, Object> all = resultItem.getAll();
        String tableName;
        if (this.table == null || ValidateUtils.isEmpty(tableName = this.table.getTableName(task))) {
            tableName = defaultTableName;
        }
        StringBuilder sql = new StringBuilder("insert into " + tableName + " (");
        if (generator != null) {
            sql.append("id").append(",");
        }
        Map<String, Object> item = (Map<String, Object>) all.get(tableName);
        Set<String> set = item.keySet();
        if (set.size() > 0) {
            sql.append(String.join(",", set));
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
            objects[i++] = item.get(key);
        }
        sql = new StringBuilder(sql.substring(0, sql.length() - 1));
        sql.append(")");
        onInsert(sql.toString(), objects);
    }
}
