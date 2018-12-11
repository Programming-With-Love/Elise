package site.zido.elise.task.model;

import java.util.List;

public final class Model {
    private String name;
    private List<Target> targets;
    private List<Helper> helpers;
    private Action region;
    private List<ModelField> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Target> getTargets() {
        return targets;
    }

    public void setTargets(List<Target> targets) {
        this.targets = targets;
    }

    public List<Helper> getHelpers() {
        return helpers;
    }

    public void setHelpers(List<Helper> helpers) {
        this.helpers = helpers;
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
}
