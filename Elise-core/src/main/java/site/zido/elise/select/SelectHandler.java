package site.zido.elise.select;

import site.zido.elise.processor.ResponseContextHolder;
import site.zido.elise.task.model.Action;

import java.util.List;

/**
 * The interface SelectHandler.
 *
 * @author zido
 */
public interface SelectHandler {
    List<Object> select(ResponseContextHolder response, Object partition, Action action) throws SelectorMatchException;
}
