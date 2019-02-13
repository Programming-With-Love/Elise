package site.zido.elise.test;

import org.junit.Assert;
import org.junit.Test;
import site.zido.elise.test.utils.TemplateUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TestServer {
    @Test
    public void testStart() throws MalformedURLException {
        Server server = new Server();
        server.start();
        HttpURLConnection currentConnection = null;
        URL url = new URL("http://127.0.0.1:8080/one");
        boolean success = false;
        try {
            currentConnection = (HttpURLConnection) url.openConnection();
            currentConnection.setUseCaches(false);
            InputStream actualStream = currentConnection.getInputStream();
            success = true;
            String html = TemplateUtils.createHtml("one.html");
            BufferedReader reader = new BufferedReader(new InputStreamReader(actualStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            Assert.assertEquals(html, sb.toString());
        } catch (Exception ignore) {
        } finally {
            if (currentConnection != null) {
                currentConnection.disconnect();
            }
            server.stop();
        }
        Assert.assertTrue("server can't start", success);
    }
}
