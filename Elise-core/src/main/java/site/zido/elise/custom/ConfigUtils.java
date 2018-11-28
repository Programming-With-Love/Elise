package site.zido.elise.custom;

public class ConfigUtils {
    private ConfigUtils() {
    }

    public <T> T mergeConfig(String key, Config... config) {
        T result = null;
        for (Config c : config) {
            result = c.get(key);
        }
        return result;
    }
}
