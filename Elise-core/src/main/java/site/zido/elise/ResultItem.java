package site.zido.elise;

import site.zido.elise.pipeline.Saver;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 结果集
 *
 * @author zido
 */
public class ResultItem implements Iterator {
    private Map<String, List<String>> fields = new LinkedHashMap<>();

    private Request request;

    private Saver saver;
    private Task task;

    public ResultItem(Task task, Saver saver) {
        this.saver = saver;
        this.task = task;
    }

    public Object get(String key) {
        Object o = fields.get(key);
        if (o == null) {
            return null;
        }
        return fields.get(key);
    }

    public Map<String, List<String>> getAll() {
        return fields;
    }

    public ResultItem put(String key, List<String> value) {
        fields.put(key, value);
        return this;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public boolean hasNext() {
        return saver.hasNext(task, this);
    }

    @Override
    public Object next() {
        return saver.next(task, this);
    }
}
