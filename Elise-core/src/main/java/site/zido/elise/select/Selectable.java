package site.zido.elise.select;

import java.util.List;

public interface Selectable extends Matchable {
    List<Fragment> select(Selector selector);
}
