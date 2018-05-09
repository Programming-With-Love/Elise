package site.zido.elise.selector;

import java.util.List;

/**
 * 查询普通文本的selector
 *
 * @author zido
 * @date 2018/05/13
 */
public interface Selector {

    String select(String text);

    List<String> selectList(String text);
}
