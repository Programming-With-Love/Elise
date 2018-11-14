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

    public ResultItem() {
    }

    public Object get(String key) {
        Object o = fields.get(key);
        if (o == null) {
            return null;
        }
        return fields.get(key);
    }

    public Map<String, List<Fragment>> getAll() {
        return fields;
    }

    public ResultItem put(String key, List<Fragment> value) {
        fields.put(key, value);
        return this;
    }


}
