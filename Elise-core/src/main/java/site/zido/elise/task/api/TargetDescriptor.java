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
    //TODO support html?
//    Matchable<TargetDescriptor> html();
    private Action targetAction;
    private TargetDescriptor top;

    public TargetDescriptor(Action action) {
        this.targetAction = action;
    }

    /**
     * Match url target descriptor.
     *
     * @return the target descriptor
     */
    public TargetDescriptor matchUrl(String regex) {
        if(targetAction.getToken() != null){
            throw new RepeatMatchException();
        }
        targetAction.setToken(E.Action.MATCH_LINK);
        targetAction.setExtras(new Object[]{regex});
        targetAction.setSource(Source.URL);
        return this;
    }

    /**
     * Status code target descriptor.
     *
     * @param numberMatchExpress the matcher
     * @return the target descriptor
     */
    public TargetDescriptor matchStatusCode(String numberMatchExpress) {
        if(targetAction.getToken() != null){
            throw new RepeatMatchException();
        }
        targetAction.setToken(E.Action.MATCH_NUMBER);
        targetAction.setExtras(new Object[]{numberMatchExpress});
        List<Action> actions = new LinkedList<>();
        targetAction.setChildren(actions);
        targetAction.setSource(Source.CODE);
        return this;
    }

    public TargetDescriptor and(){
        Action action = new Action();
        List<Action> children = new LinkedList<>();
        this.targetAction.setChildren(children);
        children.add(action);
        return new TargetDescriptor(action);
    }

    public TargetDescriptor or(){

    }
}
