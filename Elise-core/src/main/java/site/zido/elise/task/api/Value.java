package site.zido.elise.task.api;

import site.zido.elise.task.model.ModelField;

/**
 * The interface Value.
 *
 * @author zido
 */
public class Value {
    private ModelField field;
    public Value(ModelField field){
        this.field = field;
    }
    /**
     * Save value.
     *
     * @param name the name
     * @return the value
     */
    Value save(String name){
        field.setName(name);
        return this;
    }

    /**
     * Nullable value.
     *
     * @param nullable the nullable
     * @return the value
     */
    Value nullable(boolean nullable){
        field.setNullable(nullable);
        return this;
    }
}
