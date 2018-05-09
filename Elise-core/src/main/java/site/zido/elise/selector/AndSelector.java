package site.zido.elise.selector;

import site.zido.elise.utils.ValidateUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AndSelector implements Selector {
    private List<Selector> selectors = new ArrayList<>();

    public AndSelector(List<Selector> selectors) {
        this.selectors = selectors;
    }

    public AndSelector(Selector... selectors) {
        this.selectors.addAll(Arrays.asList(selectors));
    }

    @Override
    public String select(String text) {
        for (Selector selector : selectors) {
            if (text == null) {
                return null;
            }
            text = selector.select(text);
        }
        return text;
    }

    @Override
    public List<String> selectList(String text) {
        List<String> results = new ArrayList<>();
        boolean first = true;
        for (Selector selector : selectors) {
            if (first) {
                results = selector.selectList(text);
                first = false;
            } else {
                List<String> resultsTemp = new ArrayList<>();
                for (String result : results) {
                    resultsTemp.addAll(selector.selectList(result));
                }
                results = resultsTemp;
                if (ValidateUtils.isEmpty(results)) {
                    return results;
                }
            }
        }
        return results;
    }
}
