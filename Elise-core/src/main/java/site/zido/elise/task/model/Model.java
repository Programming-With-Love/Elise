package site.zido.elise.task.model;

import java.util.List;

public final class Model {
    private String name;
    private List<Action> targets;
    private List<Action> helpers;
    private Partition partition;
    private List<ModelField> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Action> getTargets() {
        return targets;
    }

    public void setTargets(List<Action> targets) {
        this.targets = targets;
    }

    public List<Action> getHelpers() {
        return helpers;
    }

    public void setHelpers(List<Action> helpers) {
        this.helpers = helpers;
    }

    public Partition getPartition() {
        return partition;
    }

    public void setPartition(Partition partition) {
        this.partition = partition;
    }

    public List<ModelField> getFields() {
        return fields;
    }

    public void setFields(List<ModelField> fields) {
        this.fields = fields;
    }
}
