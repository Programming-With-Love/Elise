package com.hnqc.ironhand.downloader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hnqc.ironhand.proxy.Proxy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * ProxyReader
 *
 * @author zido
 * @date 2018/04/28
 */
public class ProxyReader {
    private List<Proxy> proxies;

    public ProxyReader(InputStream stream) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        this.proxies = mapper.readValue(stream, new TypeReference<List<Proxy>>() {
        });
    }

    public ProxyReader() {
        this.proxies = new ArrayList<>();
    }

    public ProxyReader addProxy(Proxy proxy) {
        this.proxies.add(proxy);
        return this;
    }

    public List<Proxy> getProxies() {
        return proxies;
    }
}
