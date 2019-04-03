package site.zido.elise.downloader;

import site.zido.elise.custom.Config;
import site.zido.elise.custom.GlobalConfig;
import site.zido.elise.task.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The type Default downloader factory.
 *
 * @author zido
 */
public final class DefaultDownloaderFactory extends AbstractDownloaderFactory {
    private Map<String, DownloaderFactory> factoryMap = new HashMap<>();

    /**
     * Instantiates a new Default downloader factory.
     */
    public DefaultDownloaderFactory() {
        registerFactory("httpclient", new HttpClientDownloaderFactory());
    }

    @Override
    public Downloader create(Task task) {
        final Config config = task.getConfig();
        final String key = config.get(GlobalConfig.KEY_DOWNLOAD_MODE);
        final DownloaderFactory factory = factoryMap.get(key);
        if (key == null) {
            throw new RuntimeException("not wrap any factory");
        }
        return factory.create(task);
    }

    /**
     * Register factory default downloader factory.
     *
     * @param key     the key
     * @param factory the factory
     * @return the default downloader factory
     */
    public void registerFactory(String key, DownloaderFactory factory) {
        factoryMap.put(key, factory);
    }

    /**
     * Key set set.
     *
     * @return the set
     */
    public Set<String> keySet() {
        return factoryMap.keySet();
    }
}
