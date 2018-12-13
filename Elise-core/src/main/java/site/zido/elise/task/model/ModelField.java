package site.zido.elise.task.model;

/**
 * The Model field.
 *
 * @author zido
 */
public final class ModelField {
    /**
     * field name
     */
    private String name;

    private boolean nullable;

    private Action action;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
