package site.zido.elise.custom;

import site.zido.elise.utils.Asserts;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Properties;

/**
 * The type Mapped config.
 *
 * @author zido
 */
public class MappedConfig extends HashMap<String, Object> implements Config {
    private static final long serialVersionUID = 8315976702547630336L;

    /**
     * Instantiates a new Mapped config.
     */
    public MappedConfig() {
    }

    @Override
    public void from(Reader reader) throws IOException {
        Asserts.notNull(reader, "can't read config from a null reader");
        final Properties props = new Properties();
        try {
            props.load(reader);
        } finally {
            reader.close();
        }
        for (Entry<Object, Object> entry : props.entrySet()) {
            this.put((String) entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void from(InputStream in) throws IOException {
        Asserts.notNull(in, "can't read config from a null input stream");
        final Properties props = new Properties();
        try {
            props.load(in);
        } finally {
            in.close();
        }
        for (Entry<Object, Object> entry : props.entrySet()) {
            this.put((String) entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void set(String key, Object value) {
        super.put(key, value);
    }

    @Override
    public <T> T get(String key) {
        return (T) super.get(key);
    }
}
