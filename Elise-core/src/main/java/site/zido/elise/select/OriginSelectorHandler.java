package site.zido.elise.select;

import site.zido.elise.processor.ResponseContextHolder;
import site.zido.elise.task.api.Source;
import site.zido.elise.task.model.Action;

import java.util.Collections;
import java.util.List;

public class OriginSelectorHandler implements SelectHandler {
    @Override
    public List<Object> select(ResponseContextHolder response, Object partition, Action action) throws SelectorMatchException {
        String source = action.getSource();
        if (Source.matchSource(source, Source.URL)) {
           return Collections.singletonList(response.getUrl());
        }else if(Source.matchSource(source,Source.CODE)){
            return Collections.singletonList(response.getStatusCode());
        }else if(Source.matchSource(source,Source.HTML,Source.TEXT)){
            return Collections.singletonList(response.getHtml());
        }
        return null;
    }
}
