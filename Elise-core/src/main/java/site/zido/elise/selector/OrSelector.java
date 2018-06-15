package site.zido.elise.selector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The type Or selector.
 *
 * @author zido
 */
public class OrSelector implements Selector {
    private List<Selector> selectors = new ArrayList<Selector>();

    /**
     * Instantiates a new Or selector.
     *
     * @param selectors the selectors
     */
    public OrSelector(Selector... selectors) {
        Collections.addAll(this.selectors, selectors);
    }

    /**
     * Instantiates a new Or selector.
     *
     * @param selectors the selectors
     */
    public OrSelector(List<Selector> selectors) {
        this.selectors = selectors;
    }

    @Override
    public String select(String text) {
        for (Selector selector : selectors) {
            String result = selector.select(text);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public List<String> selectList(String text) {
        List<String> results = new ArrayList<>();
        for (Selector selector : selectors) {
            List<String> strings = selector.selectList(text);
            results.addAll(strings);
        }
        return results;
    }
}
