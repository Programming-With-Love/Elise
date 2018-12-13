package site.zido.elise.task.model;

import java.util.List;

public final class Model {
    private String name;
    private List<Action> targets;
    private List<Action> helpers;
    private Action region;
    private List<ModelField> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Action getRegion() {
        return region;
    }

    public void setRegion(Action region) {
        this.region = region;
    }

    public List<ModelField> getFields() {
        return fields;
    }

    public void setFields(List<ModelField> fields) {
        this.fields = fields;
    }

    public List<Action> getTarget() {
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
}
