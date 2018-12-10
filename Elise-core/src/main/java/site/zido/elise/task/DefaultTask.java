package site.zido.elise.task;

import site.zido.elise.custom.Config;
import site.zido.elise.select.configurable.ResponseHandler;

/**
 * default extractor task
 * <br>
 *
 * @author zido
 */
public class DefaultTask implements Task {
    private Long id;
    private ResponseHandler responseHandler;
    private Config config;

    /**
     * Instantiates a new Default task.
     */
    public DefaultTask() {

    }

    /**
     * Instantiates a new Default task.
     *
     * @param id              the id
     * @param responseHandler the extractor
     * @param config          the config
     */
    public DefaultTask(Long id, ResponseHandler responseHandler, Config config) {
        this.id = id;
        this.responseHandler = responseHandler;
        this.config = config;
    }

    @Override
    public long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     * @return the id
     */
    public DefaultTask setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ResponseHandler getHandler() {
        return responseHandler;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    /**
     * Sets config.
     *
     * @param config the config
     * @return the config
     */
    public DefaultTask setConfig(Config config) {
        this.config = config;
        return this;
    }

    /**
     * Gets def extractor.
     *
     * @return the def extractor
     */
    public ResponseHandler getResponseHandler() {
        return responseHandler;
    }

    /**
     * Sets def extractor.
     *
     * @param responseHandler the def extractor
     * @return the def extractor
     */
    public DefaultTask setResponseHandler(ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
        return this;
    }
}
