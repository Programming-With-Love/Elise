package site.zido.elise;

import site.zido.elise.select.Fragment;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 结果集
 *
 * @author zido
 */
public class ResultItem {
    private Map<String, List<Fragment>> fields = new LinkedHashMap<>();

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

    /**
     * Gets all.
     *
     * @return the all
     */
    public Map<String, List<Fragment>> getAll() {
        return fields;
    }

    /**
     * Put result item.
     *
     * @param key   the key
     * @param value the value
     * @return the result item
     */
    public ResultItem put(String key, List<Fragment> value) {
        fields.put(key, value);
        return this;
    }


}
