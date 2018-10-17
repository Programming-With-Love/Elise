package site.zido.elise.select;

import java.util.List;

public interface Selectable {
    List<String> select(Selector selector);

    boolean match(Selector selector);

}
