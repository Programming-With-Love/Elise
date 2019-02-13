package site.zido.elise.test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import site.zido.elise.test.utils.TemplateUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class StaticHandler implements HttpHandler {
    public static HttpHandler render(String filename) {
        return new WrapHandler(filename.replaceAll("/", File.separator));
    }

    public abstract String getPath();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");

        httpExchange.sendResponseHeaders(200, 0);
        String html = TemplateUtils.createHtml(getPath());
        httpExchange.getResponseBody().write(html.getBytes(StandardCharsets.UTF_8));
        httpExchange.getResponseBody().close();
    }

    public static class WrapHandler extends StaticHandler {
        private String filename;

        public WrapHandler(String filename) {
            this.filename = filename;
        }

        @Override
        public String getPath() {
            return filename;
        }
    }
}
