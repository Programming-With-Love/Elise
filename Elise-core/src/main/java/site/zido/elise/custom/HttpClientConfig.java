package site.zido.elise.custom;

import java.util.Map;

/**
 * The type Http client config.
 *
 * @author zido
 */
public class HttpClientConfig extends GlobalConfig {
    private static final long serialVersionUID = -7610251519485407931L;

    /**
     * Instantiates a new Http client config.
     */
    public HttpClientConfig() {
    }

    /**
     * Instantiates a new Http client config.
     *
     * @param config the config
     */
    public HttpClientConfig(Map<String, Object> config) {
        super(config);
    }

}
