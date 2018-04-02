package com.hnqc.ironhand.spider.selector;

public class Selectors {
    public static CssSelector $(String selector) {
        return new CssSelector(selector);
    }

    public static CssSelector $(String selector, String attrName) {
        return new CssSelector(selector, attrName);
    }

    public static XPathSelector xpath(String xpath) {
        return new XPathSelector(xpath);
    }

    public static RegexSelector regex(String regex) {
        return new RegexSelector(regex);
    }

    public static AndSelector and(Selector... selectors) {
        return new AndSelector(selectors);
    }

    public static OrSelector or(Selector... selectors) {
        return new OrSelector(selectors);
    }
}
