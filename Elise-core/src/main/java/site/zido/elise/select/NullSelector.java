package site.zido.elise.select;

import java.util.Collections;
import java.util.List;

public class NullSelector implements Selector {
    @Override
    public List<String> select(String text) {
        return Collections.singletonList(text);
    }
}
