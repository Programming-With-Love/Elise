package site.zido.elise.processor;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.zido.elise.http.Response;
import site.zido.elise.select.FieldType;
import site.zido.elise.select.Fragment;
import site.zido.elise.select.Selector;
import site.zido.elise.select.SelectorMatchException;
import site.zido.elise.task.Task;
import site.zido.elise.task.model.Action;
import site.zido.elise.task.model.Model;
import site.zido.elise.task.model.ModelField;
import site.zido.elise.task.model.Partition;
import site.zido.elise.utils.EventUtils;
import site.zido.elise.utils.ValidateUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * model page processor with extractor,
 *
 * @author zido
 */
public class DefaultResponseProcessor implements ListenableResponseProcessor {
    private static Logger LOGGER = LoggerFactory.getLogger(DefaultResponseProcessor.class);
    private static final String HTTP_LABEL = "http";
    private Set<ProcessorEventListener> listeners = new HashSet<>();
    private Saver saver;
    private Map<String, Selector> selectors = new HashMap<>();

    /**
     * Instantiates a new Default response handler.
     *
     * @param saver the saver
     */
    public DefaultResponseProcessor(Saver saver) {
        this.saver = saver;
    }

    private List<Object> processSelectors(final ResponseContextHolder holder,
                                          final Action action,
                                          Object partition) throws SelectorMatchException {
        List<Object> partitions;
        Selector selector = selectors.get(action.getToken());
        if (selector != null) {
            partitions = selector.selectObj(holder, partition, action);
//            switch (action.getSource()) {
//                case Source.URL:
//                    partitions = selector.selectObj(holder, action.getExtras());
//                    break;
//                case Source.HTML:
//                    partitions = selector.selectObj((), action.getExtras());
//                    break;
//                case Source.BODY:
//                    partitions = selector.selectObj(response.getUrl(), response.getBody(), action.getExtras());
//                    break;
//                case Source.CODE:
//                    partitions = selector.selectObj(response.getUrl(), response.getStatusCode(), action.getExtras());
//                    break;
//                case Source.TEXT:
//                    partitions = selector.selectObj(holder.getHtml(), action.getExtras());
//                    break;
//                case Source.PARTITION:
//                    if (partition == null) {
//                        return null;
//                    }
//                    List<Object> result = selector.selectObj(partition, action.getExtras());
//                    if (!ValidateUtils.isEmpty(result)) {
//                        partitions.addAll(result);
//                    }
//                    break;
//                default:
//            }
            if (ValidateUtils.isEmpty(partitions)) {
                return null;
            }
            List<Action> children = action.getChildren();
            List<Object> selectResults = new LinkedList<>();
            for (Object o : partitions) {
                for (Action child : children) {
                    List<Object> results = processSelectors(holder, child, o);
                    if (!ValidateUtils.isEmpty(results)) {
                        selectResults.addAll(results);
                    }
                }
            }
            return selectResults;
        }
        return null;
    }

    @Override
    public Set<String> process(Task task, final Response response) throws SelectorMatchException {
        Model model = task.getModel();
        ResponseContextHolder holder = new ResponseContextHolder(response, task.getConfig());
        //find helpers
        List<Action> helpers = model.getHelpers();
        Set<String> links = new HashSet<>();
        for (Action helper : helpers) {
            List<Object> results = processSelectors(holder, helper, null);
            if (!ValidateUtils.isEmpty(results)) {
                links.addAll(processLinks(results, response.getUrl()));
            }
        }
        //judge target
        List<Action> targets = model.getTargets();
        boolean isTarget = false;
        for (Action target : targets) {
            if (!ValidateUtils.isEmpty(processSelectors(holder, target, null))) {
                isTarget = true;
                break;
            }
        }
        if (!isTarget) {
            return links;
        }
        // match field
        Partition partition = model.getPartition();
        List<ResultItem> resultItems = new LinkedList<>();
        if (partition != null) {
            Action action = partition.getAction();
            List<Object> partitions = null;
            if (action != null) {
                partitions = processSelectors(holder, action, null);
            }
            if (!ValidateUtils.isEmpty(partitions)) {
                for (Object part : partitions) {
                    for (ModelField field : partition.getFields()) {
                        List<Action> actions = field.getActions();
                        List<Object> values = new LinkedList<>();
                        for (Action fieldAction : actions) {
                            List<Object> results = processSelectors(holder, fieldAction, part);
                            if (!ValidateUtils.isEmpty(results)) {
                                values.addAll(results);
                            }
                        }
                        if (!ValidateUtils.isEmpty(values)) {
                            values = processField(values, field.getValueType());
                            if (!ValidateUtils.isEmpty(values)) {
                                ResultItem item = new ResultItem();
                                item.put(field.getName(), values, field.getValueType());
                                resultItems.add(item);
                            } else if (field.isNullable()) {
                                resultItems.add(new ResultItem());
                            }
                        }
                    }
                }
            }
        }
        List<ModelField> fields = model.getFields();
        if (fields != null) {
            boolean isAddNew = ValidateUtils.isEmpty(resultItems);
            ResultItem item = null;
            for (ModelField field : fields) {
                List<Action> actions = field.getActions();
                List<Object> values = new LinkedList<>();
                for (Action action : actions) {
                    List<Object> results = processSelectors(holder, action, null);
                    if (!ValidateUtils.isEmpty(results)) {
                        values.addAll(results);
                    }
                }
                if (!ValidateUtils.isEmpty(values)) {
                    values = processField(values, field.getValueType());
                    if (!isAddNew && !ValidateUtils.isEmpty(values)) {
                        for (ResultItem resultItem : resultItems) {
                            resultItem.put(field.getName(), values, field.getValueType());
                        }
                    } else if (!ValidateUtils.isEmpty(values)) {
                        if (item == null) {
                            item = new ResultItem();
                        }
                        item.put(field.getName(), values, field.getValueType());
                    }
                }
            }
            if (isAddNew && item != null) {
                resultItems.add(item);
            }
        }
        if (!ValidateUtils.isEmpty(resultItems)) {
            for (ResultItem resultItem : resultItems) {
                if (resultItem != null) {
                    try {
                        saver.save(resultItem, task);
                        EventUtils.mustNotifyListeners(listeners, listener -> listener.onSaveSuccess(task, resultItem));
                    } catch (Throwable e) {
                        EventUtils.mustNotifyListeners(listeners, listener -> listener.onSaveError(task, resultItem));
                        LOGGER.error("saver have made a exception", e);
                    }
                }
            }
        } else {
            LOGGER.info("response not find anything, response {}", response.getUrl());
        }
        return links;
    }


    private List<Object> processField(List<Object> values, FieldType fieldType) {
        if (!values.isEmpty()) {
            switch (fieldType) {
                case RICH:
                    return values.stream().filter(v -> v instanceof Node).map(value -> {
                        Node v = (Node) value;
                        Fragment fragment = new Fragment();
                        fragment.add(v);
                        return fragment;
                    }).collect(Collectors.toList());
                case TEXT:
                    return values.stream().map(v -> {
                        if (v instanceof Element) {
                            return ((Element) v).text();
                        } else {
                            return v.toString();
                        }
                    }).collect(Collectors.toList());
                case NUMBER:
                    return values.stream().map(v -> {
                        String text;
                        if (v instanceof Element) {
                            text = ((Element) v).text();
                        } else {
                            text = v.toString();
                        }
                        if (text.matches("[0-9]")) {
                            return Integer.parseInt(text);
                        } else {
                            return null;
                        }
                    }).filter(Objects::nonNull).collect(Collectors.toList());
                case ORIGIN:
                    return values;
                case XML:
                case HTML:
                default:
                    return values.stream().filter(node -> node instanceof Node).map(v -> ((Node) v).outerHtml()).collect(Collectors.toList());
            }
        }
        return null;
    }

    private Set<String> processLinks(List<Object> links, String baseUrl) {
        return links.stream().filter(obj -> obj instanceof String).map(obj -> {
            String link = (String) obj;
            link = link.replaceAll("#.*$", "").replace("&amp;", "&");
            if (link.startsWith(HTTP_LABEL)) {
                //已经是绝对路径的，不再处理
                return link;
            }
            try {
                return new URL(new URL(baseUrl), link).toString();
            } catch (MalformedURLException e) {
                LOGGER.error("An error occurred while processing the link,base:[{}],spec:[{}]", baseUrl, link);
                return null;
            }
        }).filter(link -> !ValidateUtils.isEmpty(link)).collect(Collectors.toSet());
    }

    @Override
    public void addEventListener(ProcessorEventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeEventListener(ProcessorEventListener listener) {
        listeners.remove(listener);
    }

    public void registorSelector(String token, Selector selector) {
        selectors.put(token, selector);
    }
}
