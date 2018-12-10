package site.zido.elise.select.api;

import site.zido.elise.select.LinkSelector;

/**
 * The interface Help descriptor.
 *
 * @author zido
 */
public interface HelpDescriptor {
    /**
     * Filter help descriptor.
     *
     * @param linkSelector the link selector
     * @return the help descriptor
     */
    HelpDescriptor filter(LinkSelector linkSelector);
}
