package site.zido.elise.custom;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Config utils.
 *
 * @author zido
 */
public class ConfigUtils {
    private ConfigUtils() {
    }

    /**
     * Merge config t.
     *
     * @param <T>    the type parameter
     * @param key    the key
     * @param config the config
     * @return the t
     */
    public static <T> T mergeConfig(String key, Config... config) {
        T result = null;
        for (Config c : config) {
            final T o = c.get(key);
            if (o != null) {
                result = o;
            }
        }
        return result;
    }

    /**
     * Merge config config.
     *
     * @param config the config
     * @return the config
     */
    public static Config mergeConfig(Config... config) {
        Map<String, Object> result = new HashMap<>();
        for (Config c : config) {
            if (c != null) {
                for (String s : c.keySet()) {
                    final Object value = c.get(s);
                    if (value != null) {
                        result.put(s, value);
                    }
                }
            }
        }
        return new MappedConfig(result);
    }
}
