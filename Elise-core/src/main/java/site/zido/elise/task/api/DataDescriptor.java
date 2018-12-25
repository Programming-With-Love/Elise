package site.zido.elise.task.api;

import site.zido.elise.E;
import site.zido.elise.select.FieldType;
import site.zido.elise.task.model.Action;
import site.zido.elise.task.model.ModelField;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Data descriptor.
 * describe how to extract data.
 *
 * @author zido
 */
public class DataDescriptor {
    private List<ModelField> fields;

    public DataDescriptor(List<ModelField> fields) {
        this.fields = fields;
    }

    /**
     * Html element selectable.
     *
     * @return the element selectable
     */
    public ElementSelectable html() {
        ModelField field = new ModelField();
        fields.add(field);
        List<Action> actions = new LinkedList<>();
        field.setActions(actions);
        return new ElementSelectable(Source.HTML, field, actions);
    }

    /**
     * get data from url
     *
     * @return the value descriptor
     */
    public Value url() {
        return getValue(Source.URL, FieldType.TEXT);
    }

    /**
     * get data from status code
     *
     * @return the value descriptor
     */
    public Value statusCode() {
        return getValue(Source.CODE, FieldType.NUMBER);
    }

    private Value getValue(String source, FieldType type) {
        ModelField field = new ModelField();
        fields.add(field);
        Action action = new Action();
        action.setSource(source);
        action.setToken(E.Action.SELECT_ORIGIN);
        field.setActions(Collections.singletonList(action));
        field.setValueType(type);
        return new Value(field);
    }
}
