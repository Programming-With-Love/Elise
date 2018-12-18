package site.zido.elise.task.api;

import site.zido.elise.select.ElementSelector;
import site.zido.elise.task.model.Action;
import site.zido.elise.task.model.Model;
import site.zido.elise.task.model.ModelField;
import site.zido.elise.task.model.Partition;

import java.util.LinkedList;
import java.util.List;

public class DefaultSelectableResponse implements SelectableResponse {
    private final Model model = new Model();

    @Override
    public SelectableResponse modelName(String name) {
        model.setName(name);
        return this;
    }

    @Override
    public TargetDescriptor asTarget() {
        List<Action> targets = model.getTargets();
        if (targets == null) {
            targets = new LinkedList<>();
            model.setTargets(targets);
        }
        return new TargetDescriptor(targets);
    }

    @Override
    public HelpDescriptor asHelper() {
        List<Action> helpers = model.getHelpers();
        if (helpers == null) {
            helpers = new LinkedList<>();
            model.setHelpers(helpers);
        }
        return new HelpDescriptor(helpers);
    }

    @Override
    public DataDescriptor asContent() {
        List<ModelField> fields = model.getFields();
        if (fields == null) {
            fields = new LinkedList<>();
            model.setFields(fields);
        }
        return new DataDescriptor(fields);
    }

    @Override
    public PartitionDescriptor asPartition(ElementSelector selector) {
        Partition partition = new Partition();
        model.setPartition(partition);
        partition.setAction(selector);
        selector.setSource(Source.HTML);
        selector.setChildren(null);
        return new PartitionDescriptor(partition);
    }

    public Model getModel() {
        return this.model;
    }
}
