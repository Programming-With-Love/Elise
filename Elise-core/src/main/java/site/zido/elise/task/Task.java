package site.zido.elise.task;

import site.zido.elise.custom.Config;
import site.zido.elise.select.configurable.ResponseHandler;
import site.zido.elise.task.model.Model;

/**
 * Task interface
 *
 * @author zido
 */
public interface Task {
    /**
     * Get task id
     *
     * @return id id
     */
    long getId();

    /**
     * get the model extractor
     *
     * @return extractors model extractor
     */
    ResponseHandler getHandler();

    Model getModel();

    /**
     * Gets config.
     *
     * @return the config
     */
    Config getConfig();
}
