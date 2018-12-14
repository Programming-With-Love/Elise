package site.zido.elise.task.api;

import site.zido.elise.select.matcher.Matcher;
import site.zido.elise.select.NumberMatcherSelector;

/**
 * The interface Target descriptor.
 *
 * @author zido
 */
public interface TargetDescriptor {
    //TODO support html?
//    Matchable<TargetDescriptor> html();

    /**
     * Match url target descriptor.
     *
     * @param matcher the matcher
     * @return the target descriptor
     */
    TargetDescriptor matchUrl(Matcher matcher);

    /**
     * Status code target descriptor.
     *
     * @param matcher the matcher
     * @return the target descriptor
     */
    TargetDescriptor statusCode(NumberMatcherSelector matcher);
}
