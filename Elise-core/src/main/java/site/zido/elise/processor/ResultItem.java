package site.zido.elise.processor;

import site.zido.elise.select.FieldType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 结果集
 *
 * @author zido
 */
public class ResultItem {
    private String name;
    private Map<String, List<Object>> fields = new LinkedHashMap<>();
    private Map<String, FieldType> fieldTypeMap = new LinkedHashMap<>();

    /**
     * Instantiates a new Result item.
     */
    public ResultItem() {
    }

    /**
     * Get object.
     *
     * @param key the key
     * @return the object
     */
    public Object get(String key) {
        Object o = fields.get(key);
        if (o == null) {
            return null;
        }
        return fields.get(key);
    }

    public FieldType getType(String key) {
        return fieldTypeMap.get(key);
    }

    public ResultItem setType(String key, FieldType type) {
        fieldTypeMap.put(key, type);
        return this;
    }

    /**
     * Gets all.
     *
     * @return the all
     */
    public Map<String, List<Object>> getAll() {
        return fields;
    }

    /**
     * Put result item.
     *
     * @param key   the key
     * @param value the value
     * @return the result item
     */
    public ResultItem put(String key, List<Object> value) {
        fields.put(key, value);
        return this;
    }

    public ResultItem put(String key, List<Object> value, FieldType type) {
        put(key, value);
        setType(key, type);
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
