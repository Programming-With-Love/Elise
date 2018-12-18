package site.zido.elise.select;

import site.zido.elise.E;

public class HtmlLinkSelector extends ElementSelector {
    public HtmlLinkSelector(String regex) {
        super(E.Action.LINK_SELECTOR);
        super.setExtras(new String[]{regex});
    }
}
