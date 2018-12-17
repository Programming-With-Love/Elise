package site.zido.elise.task.api;

import site.zido.elise.select.*;
import site.zido.elise.task.model.Action;
import site.zido.elise.task.model.ModelField;

import java.util.LinkedList;
import java.util.List;

/**
 * The interface Element selectable.
 *
 * @author zido
 */
public class ElementSelectable {
    private String source;
    private ModelField field;
    private List<Action> actions;
    public ElementSelectable(String source, ModelField field,List<Action> actions){
        this.source = source;
        this.field = field;
        this.actions = actions;
    }
    /**
     * Partition element selectable.
     *
     * @param elementSelector the element selector
     * @return the element selectable
     */
    public ElementSelectable partition(ElementSelector elementSelector){
        elementSelector.setSource(source);
        elementSelector.setChildren(new LinkedList<>());
        actions.add(elementSelector);
        elementSelector.setChildren(new LinkedList<>());
        return new ElementSelectable(Source.PARTITION,field,elementSelector.getChildren());
    }

    /**
     * Select element value.
     *
     * @param selector the selector
     * @return the element value
     */
    public ElementValue select(ElementSelector selector){
        selector.setSource(source);
        actions.add(selector);
        return new ElementValue(field);
    }

    /**
     * Css element value.
     *
     * @param css the css
     * @return the element value
     */
    public ElementValue css(String css) {
        return select(new CssSelector(css));
    }

    /**
     * Xpath element value.
     *
     * @param xpath the xpath
     * @return the element value
     */
    public ElementValue xpath(String xpath) {
        return select(new XpathSelector(xpath));
    }
}
