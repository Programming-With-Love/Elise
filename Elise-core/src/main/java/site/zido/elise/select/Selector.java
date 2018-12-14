package site.zido.elise.select;

import site.zido.elise.processor.ResponseContextHolder;
import site.zido.elise.task.model.Action;

import java.util.List;

/**
 * The interface Selector.
 *
 * @author zido
 */
public interface Selector {
    List<Object> selectObj(ResponseContextHolder response, Object partition, Action action) throws SelectorMatchException;
}
