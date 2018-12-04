package site.zido.elise.custom;

import java.util.HashMap;
import java.util.Map;

public class ConfigUtils {
    private ConfigUtils() {
    }

    public <T> T mergeConfig(String key, Config... config) {
        T result = null;
        for (Config c : config) {
            final T o = c.get(key);
            if (o != null) {
                result = o;
            }
        }
        return result;
    }

    public Config mergeConfig(Config... config) {
        Map<String, Object> result = new HashMap<>();
        for (Config c : config) {
            for (String s : c.keySet()) {
                final Object value = c.get(s);
                if (value != null) {
                    result.put(s, value);
                }
            }
        }
        return new MappedConfig(result);
    }
}
