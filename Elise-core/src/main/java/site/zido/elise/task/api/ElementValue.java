package site.zido.elise.task.api;

import site.zido.elise.select.FieldType;
import site.zido.elise.task.model.ModelField;

/**
 * The interface Element value.
 *
 * @author zido
 */
public class ElementValue {
    private ModelField field;
    private ElementSelectable top;

    public ElementValue(ElementSelectable top,ModelField field) {
        this.field = field;
        this.top = top;
    }

    /**
     * Text value.
     *
     * @return the value
     */
    public Value text() {
        field.setValueType(FieldType.TEXT);
        return new Value(field);
    }

    /**
     * Rich value.
     *
     * @return the value
     */
    public Value rich() {
        field.setValueType(FieldType.RICH);
        return new Value(field);
    }

    /**
     * Xml value.
     *
     * @return the value
     */
    public Value xml() {
        field.setValueType(FieldType.XML);
        return new Value(field);
    }

    public ElementSelectable or(){
        return top;
    }
}
