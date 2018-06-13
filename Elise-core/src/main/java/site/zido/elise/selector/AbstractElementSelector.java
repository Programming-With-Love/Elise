package site.zido.elise.selector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象dom节点选择器
 *
 * @author zido
 */
public abstract class AbstractElementSelector implements Selector, ElementSelector {
    @Override
    public String select(String text) {
        if (text != null) {
            return select(Jsoup.parse(text));
        }
        return null;
    }

    @Override
    public List<String> selectList(String text) {
        if (text != null) {
            return selectList(Jsoup.parse(text));
        } else {
            return new ArrayList<>();
        }
    }

    public Element selectElement(String text) {
        if (text != null) {
            return selectElement(Jsoup.parse(text));
        }
        return null;
    }

    public List<Element> selectElements(String text) {
        if (text != null) {
            return selectElements(Jsoup.parse(text));
        }
        return new ArrayList<>();
    }

    /**
     * 从parent dom节点中选择child dom节点
     *
     * @param element parent dom
     * @return child dom
     */
    public abstract Element selectElement(Element element);

    /**
     * 从parent dom节点中选择children dom节点
     *
     * @param element parent dom
     * @return children dom
     */
    public abstract List<Element> selectElements(Element element);

    /**
     * 是否是文字
     *
     * @return true/false
     */
    public abstract boolean isText();
}
