package site.zido.elise.test;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;
import site.zido.elise.test.handlers.OneHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    public static final String ONE_PATH;
    public static final String MULTI_PATH_ENTRY;
    private static final int PORT = 8080;
    private static final String DOMAIN = "http://127.0.0.1";

    static {
        ONE_PATH = wrapPath("/one");
        MULTI_PATH_ENTRY = wrapPath("/multi/entry");
    }

    private HttpServer httpServer;

    public static String wrapPath(String path) {
        return DOMAIN + ":" + PORT + path;
    }

    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {
        HttpServerProvider provider = HttpServerProvider.provider();
        try {
            httpServer = provider.createHttpServer(new InetSocketAddress(8080), 100);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        httpServer.createContext("/one", new OneHandler());
        httpServer.createContext("/multi/entry", StaticHandler.render("/multi/entry.html"));
        httpServer.createContext("/multi/one", StaticHandler.render("/multi/one.html"));
        httpServer.createContext("/multi/two", StaticHandler.render("/multi/two.html"));
        httpServer.createContext("/multi/three", StaticHandler.render("/multi/three.html"));
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }
}
