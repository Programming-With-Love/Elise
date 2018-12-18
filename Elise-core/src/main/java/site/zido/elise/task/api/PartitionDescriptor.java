package site.zido.elise.task.api;

import site.zido.elise.task.model.ModelField;
import site.zido.elise.task.model.Partition;

import java.util.LinkedList;

public class PartitionDescriptor {
    private Partition partition;

    public PartitionDescriptor(Partition partition) {
        this.partition = partition;
    }

    public ElementSelectable field() {
        if (partition.getFields() == null) {
            partition.setFields(new LinkedList<>());
        }
        ModelField field = new ModelField();
        field.setActions(new LinkedList<>());
        partition.getFields().add(field);
        return new ElementSelectable(Source.PARTITION, field, field.getActions());
    }
}
