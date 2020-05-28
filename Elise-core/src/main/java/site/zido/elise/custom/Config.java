package site.zido.elise.custom;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

/**
 * 配置接口
 *
 * @author zido
 */
public interface Config extends Map<String, Object> {

    /**
     * 从reader中解析
     *
     * @param reader reader
     * @throws IOException ex
     */
    void from(Reader reader) throws IOException;

    /**
     * 从inputStream中解析
     *
     * @param in input stream
     * @throws IOException ex
     */
    void from(InputStream in) throws IOException;

    /**
     * 设置键值
     *
     * @param key   the key
     * @param value the value
     */
    void set(String key, Object value);

    /**
     * 获取值
     *
     * @param <T> the type parameter
     * @param key the key
     * @return the t
     */
    <T> T get(String key);
}
