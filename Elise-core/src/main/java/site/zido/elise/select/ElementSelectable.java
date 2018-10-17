package site.zido.elise.select;

import java.util.List;

public interface ElementSelectable extends Selectable {
    List<Fragment> select(ElementSelector selector);
}
