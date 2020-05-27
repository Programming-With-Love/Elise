package site.zido.elise.task.model;

import site.zido.elise.select.FieldType;

import java.util.List;
import java.util.Objects;

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

    private FieldType valueType;

    private List<Action> actions;

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

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public FieldType getValueType() {
        return valueType;
    }

    public void setValueType(FieldType valueType) {
        this.valueType = valueType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelField that = (ModelField) o;
        return nullable == that.nullable &&
            Objects.equals(name, that.name) &&
            valueType == that.valueType &&
            Objects.equals(actions, that.actions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, nullable, valueType, actions);
    }
}
