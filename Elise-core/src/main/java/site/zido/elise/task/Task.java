package site.zido.elise.task;

import site.zido.elise.custom.Config;
import site.zido.elise.task.model.Model;

/**
 * Task interface
 *
 * @author zido
 */
public interface Task {

    /**
     * get the model extractor
     *
     * @return extractors model
     */
    Model getModel();

    /**
     * Gets config.
     *
     * @return the config
     */
    Config getConfig();
}
