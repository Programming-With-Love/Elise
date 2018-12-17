package site.zido.elise.select;

import site.zido.elise.E;

public class XpathSelector extends ElementSelector {
    public XpathSelector(String xpath) {
        super(E.Action.XPATH_SELECTOR);
        super.setExtras(new Object[]{xpath});
    }
}
