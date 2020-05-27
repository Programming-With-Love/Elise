package site.zido.elise.task.api;

import site.zido.elise.E;
import site.zido.elise.task.model.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * The interface Help descriptor.
 *
 * @author zido
 */
public class HelpDescriptor {
    private List<Action> helpActions;

    public HelpDescriptor(List<Action> action) {
        this.helpActions = action;
    }

    /**
     * Filter help descriptor.
     *
     * @param regex the regex express
     * @return the help descriptor
     */
    public HelpDescriptor regex(String regex) {
        Action action = new Action();
        action.setToken(E.Action.LINK_SELECTOR);
        action.setExtras(new Object[]{regex});
        action.setSource(Source.HTML);
        helpActions.add(action);
        return this;
    }

    public HelpDescriptor and() {
        if (helpActions.isEmpty()) {
            return this;
        }
        List<Action> children = new LinkedList<>();
        Action action = helpActions.get(helpActions.size() - 1);
        action.setChildren(children);
        return new HelpDescriptor(children);
    }

    public HelpDescriptor or() {
        return this;
    }
}
