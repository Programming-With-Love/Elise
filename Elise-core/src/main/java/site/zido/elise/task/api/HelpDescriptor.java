package site.zido.elise.task.api;

import site.zido.elise.select.LinkSelectHandler;

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
    HelpDescriptor filter(LinkSelectHandler linkSelector);
}
