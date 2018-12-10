package site.zido.elise.select.api;

import site.zido.elise.select.LinkSelector;

public interface HelpDescriptor {
    HelpDescriptor filter(LinkSelector linkSelector);
}
