package site.zido.elise.saver;

import site.zido.elise.DefaultTask;
import site.zido.elise.ResultItem;
import site.zido.elise.Task;
import site.zido.elise.configurable.DefRootExtractor;
import site.zido.elise.utils.ValidateUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Abstract Sql Saver
 *
 * @author zido
 */
public abstract class AbstractSqlSaver implements Saver {
    private String defaultTableName = "elise_result";
    private IdGenerator generator;
    private Table table = new Table() {
    };

    public AbstractSqlSaver() {
    }

    public AbstractSqlSaver(Table table, IdGenerator generator) {
        this.generator = generator;
        this.table = table;
    }

    public AbstractSqlSaver setDefaultTableName(String defaultTableName) {
        this.defaultTableName = defaultTableName;
        return this;
    }

    public AbstractSqlSaver setGenerator(IdGenerator generator) {
        this.generator = generator;
        return this;
    }

    public AbstractSqlSaver setTable(Table table) {
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
    public void save(ResultItem resultItem, Task task) {
        Map<String, List<String>> all = resultItem.getAll();
        String tableName;
        if (this.table == null || ValidateUtils.isEmpty(tableName = this.table.getTableName(task))) {
            tableName = defaultTableName;
        }
        StringBuilder sql = new StringBuilder("insert into " + tableName + " (");
        if (generator != null) {
            sql.append("id").append(",");
        }
        Set<String> set = all.keySet();
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
            List<String> value = all.get(key);
            objects[i++] = value.get(0);
        }
        sql = new StringBuilder(sql.substring(0, sql.length() - 1));
        sql.append(")");
        onInsert(sql.toString(), objects);
    }

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
            if (task instanceof DefaultTask) {
                DefRootExtractor defExtractor = ((DefaultTask) task).getDefExtractor();
                return defExtractor.getName();
            }
            return null;
        }
    }
}
