package site.zido.elise.select;

import site.zido.elise.E;

public class CssSelector extends ElementSelector {
    public CssSelector(String express) {
        super(E.Action.CSS_SELECTOR);
        super.setExtras(new Object[]{express});
    }
}
