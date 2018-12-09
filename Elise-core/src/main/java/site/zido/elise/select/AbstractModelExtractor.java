package site.zido.elise.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.http.Body;
import site.zido.elise.http.Http;
import site.zido.elise.http.Response;
import site.zido.elise.processor.ResultItem;
import site.zido.elise.select.configurable.FieldExtractor;
import site.zido.elise.utils.ValidateUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Abstract model extractor,handle all selectors
 *
 * @author zido
 */
public abstract class AbstractModelExtractor implements ModelExtractor {
    private static final String HTTP_LABEL = "http";
    private static Logger LOGGER = LoggerFactory.getLogger(ModelExtractor.class);
    private static final Pattern XML_LIKED_PATTERN = Pattern.compile("(html)|(xml)", Pattern.CASE_INSENSITIVE);

    /**
     * Gets target url selectors.
     *
     * @return the target url selectors
     */
    protected abstract List<LinkSelector> getTargetUrlSelectors();

    /**
     * Gets source selector.
     *
     * @return the source selector
     */
    protected abstract ElementSelector getSourceSelector();

    /**
     * Gets help url selectors.
     *
     * @return the help url selectors
     */
    protected abstract List<LinkSelector> getHelpUrlSelectors();

    /**
     * Gets field extractors.
     *
     * @return the field extractors
     */
    protected abstract List<FieldExtractor> getFieldExtractors();

    protected boolean judgeElementSelect(Body body, ElementSelector selector) {
        Http.ContentType contentType = body.getContentType();
        String type = contentType.getType();
        return XML_LIKED_PATTERN.matcher(type).find();
    }

    @Override
    public List<ResultItem> extract(Response response) {
        List<LinkSelector> targetUrlSelectors = getTargetUrlSelectors();
        if (!ValidateUtils.isEmpty(targetUrlSelectors)) {
            if (targetUrlSelectors.stream().noneMatch(linkSelector -> linkSelector.select(response.getUrl()) != null)) {
                return new ArrayList<>();
            }
        }
        List<ResultItem> results = new ArrayList<>();
        Body body = response.getBody();
        ElementSelector sourceSelector = getSourceSelector();
        if (judgeElementSelect(body, sourceSelector)) {
            List<Node> list = sourceSelector.select(Jsoup.parse(body.toString(), response.getUrl()));
            //select from region
            for (Node node : list) {
                Map<String, List<Fragment>> item = processSingle(response, node);
                toResults(results, item);
            }
        } else {
            List<Fragment> list = sourceSelector.select(body.toString());
            //get region
            for (Fragment fragment : list) {
                String html = fragment.toString();
                Map<String, List<Fragment>> item = processSingle(response, html);
                toResults(results, item);
            }
        }
        return results;
    }


    private void toResults(List<ResultItem> results, Map<String, List<Fragment>> item) {
        if (item != null) {
            ResultItem resultItem = new ResultItem();
            for (String s : item.keySet()) {
                resultItem.put(s, item.get(s));
            }
            results.add(resultItem);
        }
    }

    @Override
    public Set<String> extractLinks(Response response) {
        Set<String> links;

        List<LinkSelector> helpUrlSelectors = getHelpUrlSelectors();
        if (ValidateUtils.isEmpty(helpUrlSelectors)) {
            return new HashSet<>(0);
        } else {
            links = new HashSet<>();
            for (LinkSelector selector : helpUrlSelectors) {
                List<Fragment> list = selector.select(response.getBody().toString());
                for (Fragment fragment : list) {
                    String link = fragment.text().replaceAll("#.*$", "");
                    links.add(link);
                }
            }
            //processing link
            links = links.stream().map(link -> {
                link = link.replace("&amp;", "&");
                if (link.startsWith(HTTP_LABEL)) {
                    //已经是绝对路径的，不再处理
                    return link;
                }
                try {
                    return new URL(new URL(response.getUrl().toString()), link).toString();
                } catch (MalformedURLException e) {
                    LOGGER.error("An error occurred while processing the link,base:[{}],spec:[{}]", response.getUrl().toString(), link);
                }
                return link;
            }).collect(Collectors.toSet());
        }
        return links;
    }


    private Map<String, List<Fragment>> processSingle(Response response, Object html) {
        List<FieldExtractor> fieldExtractors = getFieldExtractors();
        Map<String, List<Fragment>> map = new HashMap<>(fieldExtractors.size());
        for (FieldExtractor fieldExtractor : fieldExtractors) {
            List<Fragment> results = processField(fieldExtractor, response, html);
            if (ValidateUtils.isEmpty(results) && !fieldExtractor.getNullable()) {
                return null;
            }
            map.put(fieldExtractor.getName(), results);
        }
        return map;
    }


    private List<Fragment> processField(FieldExtractor fieldExtractor, Response response, Object html) {
        List<Fragment> value;
        Selector selector = fieldExtractor.getSelector();
        switch (fieldExtractor.getSource()) {
            case RAW_HTML:
                value = selector.select(response.getBody().toString());
                break;
            case URL:
                value = selector.select(response.getUrl());
                break;
            case RAW_TEXT:
                value = selector.select(response.getBody().toString());
                break;
            case REGION:
            default:
                if (html instanceof Node) {
                    value = selector.select((Element) html);
                } else {
                    value = selector.select((String) html);
                }

        }
        return value;
    }
}
