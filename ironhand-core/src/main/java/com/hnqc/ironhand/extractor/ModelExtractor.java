package com.hnqc.ironhand.extractor;

import com.hnqc.ironhand.Page;
import com.hnqc.ironhand.ResultItem;

import java.util.List;

/**
 * model extractor
 *
 * @author zido
 * @date 2018/04/18
 */
public interface ModelExtractor {
    ResultItem extract(Page page);

    List<String> extractLinks(Page page);
}
