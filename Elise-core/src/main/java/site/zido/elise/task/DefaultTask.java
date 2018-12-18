package site.zido.elise.task;

import site.zido.elise.custom.Config;
import site.zido.elise.task.api.DefaultSelectableResponse;
import site.zido.elise.task.api.ResponseHandler;
import site.zido.elise.task.model.Model;

/**
 * default extractor task
 * <br>
 *
 * @author zido
 */
public class DefaultTask implements Task {
    private Long id;
    private Config config;
    private Model model;

    /**
     * Instantiates a new Default task.
     */
    public DefaultTask() {

    }

    /**
     * Instantiates a new Default task.
     *
     * @param id     the id
     * @param model  the extractor model
     * @param config the config
     */
    public DefaultTask(Long id, Model model, Config config) {
        this.id = id;
        this.model = model;
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
    public Model getModel() {
        return this.model;
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

    public void setModel(Model model) {
        this.model = model;
    }
}
