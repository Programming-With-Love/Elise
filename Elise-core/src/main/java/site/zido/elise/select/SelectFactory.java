package site.zido.elise.select;

import org.jsoup.nodes.Element;

import java.util.List;

public interface SelectFactory {
    List<Element> selectElements(Element element, Object[] params);

    List<Object> select(Object target, Object[] params);
}
