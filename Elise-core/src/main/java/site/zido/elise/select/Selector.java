package site.zido.elise.select;

import site.zido.elise.select.matcher.Matcher;
import site.zido.elise.utils.ValidateUtils;

import java.util.List;

/**
 * The interface Selector.
 *
 * @author zido
 */
public interface Selector extends Matcher {
    /**
     * Select list.
     *
     * @param text the text
     * @return the list
     */
    List<String> select(String text);

    @Override
    default boolean matches(Object target) {
        if (target instanceof String) {
            return !ValidateUtils.isEmpty(select((String) target));
        }
        return false;
    }
}
