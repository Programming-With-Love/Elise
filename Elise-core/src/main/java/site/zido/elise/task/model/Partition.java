package site.zido.elise.task.model;

import java.util.List;

public final class Partition {
    private Action action;
    private List<ModelField> fields;

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public List<ModelField> getFields() {
        return fields;
    }

    public void setFields(List<ModelField> fields) {
        this.fields = fields;
    }
}
