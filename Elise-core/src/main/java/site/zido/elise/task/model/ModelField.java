package site.zido.elise.task.model;

import site.zido.elise.select.FieldType;

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
    /**
     * where to get it from
     */
    private String source;

    private boolean nullable;

    private FieldType valueType;

    private Action action;

    private Object[] extra;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public Object[] getExtra() {
        return extra;
    }

    public void setExtra(Object[] extra) {
        this.extra = extra;
    }

    public FieldType getValueType() {
        return valueType;
    }

    public void setValueType(FieldType valueType) {
        this.valueType = valueType;
    }
}
