package site.zido.elise.task.api;

import site.zido.elise.task.model.Action;
import site.zido.elise.task.model.Model;

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
        if(targets == null){
            model.setTargets(new LinkedList<>());
        }
        return new TargetDescriptor(targets);
    }

    @Override
    public HelpDescriptor asHelper() {
        List<Action> helpers = model.getTargets();
        if(helpers == null){
            model.setHelpers(new LinkedList<>());
        }
        return new HelpDescriptor(helpers);
    }

    @Override
    public DataDescriptor asContent() {
        return null;
    }
}
