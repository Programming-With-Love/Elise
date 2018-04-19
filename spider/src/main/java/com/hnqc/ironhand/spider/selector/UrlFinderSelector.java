package com.hnqc.ironhand.spider.selector;

import com.hnqc.ironhand.spider.configurable.ConfigurableUrlFinder;
import com.hnqc.ironhand.spider.utils.ValidateUtils;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * UrlFinderSelector
 *
 * @author zido
 * @date 2018/04/13
 */
public class UrlFinderSelector implements ElementSelector, Selector {
    private static final String EMPTY_URL_PATTERN = "http://.*";
    private Selector targetSelector;
    private Selector regionSelector;

    public UrlFinderSelector(ConfigurableUrlFinder urlFinder) {
        this(urlFinder.getValue(), urlFinder.getType(), urlFinder.getSourceRegion());
    }

    public UrlFinderSelector(String target) {
        this(target, null, null);
    }

    public UrlFinderSelector(String target, ConfigurableUrlFinder.Type type, String sourceRegion) {
        String pattern = target;
        if (pattern == null) {
            pattern = EMPTY_URL_PATTERN;
        }
        if (type == null) {
            type = ConfigurableUrlFinder.Type.REGEX;
        }

        switch (type) {
            case REGEX:
            default:
                this.targetSelector = new RegexSelector(pattern);
        }
        if (sourceRegion != null) {
            this.regionSelector = new XpathSelector(sourceRegion);
        } else {
            this.regionSelector = new NullElementSelector();
        }

    }

    @Override
    public String select(String text) {
        List<String> regions = regionSelector.selectList(text);
        if (ValidateUtils.isEmpty(regions)) {
            return null;
        }
        for (String region : regions) {
            List<String> results = selectList(region);
            if (!ValidateUtils.isEmpty(results)) {
                return results.get(0);
            }
        }
        return null;
    }

    @Override
    public List<String> selectList(String text) {
        List<String> regions = regionSelector.selectList(text);
        if (ValidateUtils.isEmpty(regions)) {
            return null;
        }
        List<String> results = new ArrayList<>();
        for (String region : regions) {
            List<String> childResults = targetSelector.selectList(region);
            if (childResults != null) {
                results.addAll(childResults);
            }
        }
        return results;
    }

    @Override
    public String select(Element element) {
        if (regionSelector instanceof AbstractElementSelector) {
            AbstractElementSelector elementSelector = (AbstractElementSelector) regionSelector;
            List<String> regions = elementSelector.selectList(element);
            if (ValidateUtils.isEmpty(regions)) {
                return null;
            }
            for (String region : regions) {
                List<String> results = selectList(region);
                if (!ValidateUtils.isEmpty(results)) {
                    return results.get(0);
                }
            }
            return null;
        } else {
            return select(element.toString());
        }
    }

    @Override
    public List<String> selectList(Element element) {
        List<String> regions;
        if (regionSelector instanceof AbstractElementSelector) {
            List<String> result = new ArrayList<>();
            AbstractElementSelector elementSelector = (AbstractElementSelector) regionSelector;
            regions = elementSelector.selectList(element);
            for (String region : regions) {
                List<String> childResults = targetSelector.selectList(region);
                if (!ValidateUtils.isEmpty(result)) {
                    result.addAll(childResults);
                }
            }
            return result;
        } else {
            return selectList(element.toString());
        }
    }
}
