package site.zido.elise.proxy;

import site.zido.elise.http.Response;
import site.zido.elise.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple ProxyProvider. Provide proxy as round-robin without heartbeat and error check. It can be used when all proxies are stable.
 *
 * @author code4crafter@gmail.com
 * Date: 17/4/16
 * Time: 10:18
 * @since 0.7.0
 */
public class SimpleProxyProvider implements ProxyProvider {

    private final List<Proxy> proxies;

    private final AtomicInteger pointer;

    public SimpleProxyProvider(List<Proxy> proxies) {
        this(proxies, new AtomicInteger(-1));
    }

    private SimpleProxyProvider(List<Proxy> proxies, AtomicInteger pointer) {
        this.proxies = proxies;
        this.pointer = pointer;
    }

    public static SimpleProxyProvider from(Proxy... proxies) {
        List<Proxy> proxiesTemp = new ArrayList<>(proxies.length);
        Collections.addAll(proxiesTemp, proxies);
        return new SimpleProxyProvider(Collections.unmodifiableList(proxiesTemp));
    }

    @Override
    public void returnProxy(Proxy proxy, Response response, Task task) {
    }

    @Override
    public Proxy getProxy(Task task) {
        return proxies.get(incrForLoop());
    }

    private int incrForLoop() {
        int p = pointer.incrementAndGet();
        int size = proxies.size();
        if (p < size) {
            return p;
        }
        while (!pointer.compareAndSet(p, p % size)) {
            p = pointer.get();
        }
        return p % size;
    }
}
