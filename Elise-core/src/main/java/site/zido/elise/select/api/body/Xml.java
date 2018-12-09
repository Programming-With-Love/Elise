package site.zido.elise.select.api.body;

import site.zido.elise.select.ElementSelector;
import site.zido.elise.select.LinkSelector;
import site.zido.elise.select.api.Matchable;
import site.zido.elise.select.api.RichableResult;
import site.zido.elise.select.api.SelectableBody;
import site.zido.elise.select.api.Text;
import site.zido.elise.select.matcher.ElementMatcher;

public interface Xml extends Text, RichableResult, SelectableBody {
    Xml saveXml();

    Xml select(ElementSelector selector);

    Xml region(ElementSelector selector);

    Xml matches(ElementMatcher elementMatcher);

    Matchable filterLinks(LinkSelector selector);

}
