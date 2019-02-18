package site.zido.elise.task;

import site.zido.elise.custom.Config;
import site.zido.elise.task.model.Model;
import site.zido.elise.utils.IdWorker;

import java.util.Objects;

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
     * @param model  the extractor model
     * @param config the config
     */
    public DefaultTask(Model model, Config config) {
        this.id = IdWorker.nextId();
        this.model = model;
        this.config = config;
    }

    public long getId() {
        return id;
    }

    @Override
    public Model getModel() {
        return this.model;
    }

    public void setModel(Model model) {
        this.model = model;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultTask that = (DefaultTask) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return model.getName() + "(id:" + this.id + ")";
    }
}
