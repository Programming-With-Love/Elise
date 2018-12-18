package site.zido.elise.task.api;

import site.zido.elise.E;
import site.zido.elise.task.model.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * The interface Target descriptor.
 *
 * @author zido
 */
public class TargetDescriptor {
    private List<Action> targetActions;

    public TargetDescriptor(List<Action> action) {
        this.targetActions = action;
    }

    /**
     * Match url target descriptor.
     *
     * @return the target descriptor
     */
    public TargetDescriptor matchUrl(String regex) {
        Action action = new Action();
        action.setToken(E.Action.MATCH_LINK);
        action.setExtras(new Object[]{regex});
        action.setSource(Source.URL);
        targetActions.add(action);
        return this;
    }

    /**
     * Status code target descriptor.
     *
     * @param numberMatchExpress the matcher
     * @return the target descriptor
     */
    public TargetDescriptor matchStatusCode(String numberMatchExpress) {
        Action action = new Action();
        action.setToken(E.Action.MATCH_NUMBER);
        action.setExtras(new Object[]{numberMatchExpress});
        action.setSource(Source.CODE);
        targetActions.add(action);
        return this;
    }

    public TargetDescriptor and() {
        if(targetActions.isEmpty()){
            return this;
        }
        List<Action> children = new LinkedList<>();
        Action action = targetActions.get(targetActions.size() - 1);
        action.setChildren(children);
        return new TargetDescriptor(children);
    }

    public TargetDescriptor or() {
        return this;
    }
}
