package site.zido.elise.task.model;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partition partition = (Partition) o;
        return Objects.equals(action, partition.action) &&
            Objects.equals(fields, partition.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, fields);
    }
}
