package site.zido.elise.downloader;

import site.zido.elise.Task;
import site.zido.elise.custom.Config;
import site.zido.elise.custom.GlobalConfig;

import java.util.HashMap;
import java.util.Map;

public final class DefaultDownloaderFactory implements DownloaderFactory {
    private Map<String, DownloaderFactory> factoryMap = new HashMap<>();

    public DefaultDownloaderFactory() {
        registerFactory("httpclient", new HttpClientDownloaderFactory());
    }

    @Override
    public Downloader create(Task task) {
        final Config config = task.modelExtractor().getConfig();
        final String key = config.get(GlobalConfig.KEY_DOWNLOAD_MODE);
        final DownloaderFactory factory = factoryMap.get(key);
        if (key == null) {
            throw new RuntimeException("not wrap any factory");
        }
        return factory.create(task);
    }

    public DefaultDownloaderFactory registerFactory(String key, DownloaderFactory factory) {
        factoryMap.put(key, factory);
        return this;
    }
}
