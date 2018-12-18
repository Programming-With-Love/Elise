package site.zido.elise.select;

import site.zido.elise.task.model.Action;

/**
 * The interface Element selector.
 *
 * @author zido
 */
public abstract class ElementSelector extends Action {
    public ElementSelector(String token) {
        super();
        super.setToken(token);
    }
}
