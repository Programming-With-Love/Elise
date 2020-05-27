package site.zido.elise.test.utils;

import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TemplateUtils {
    public static String createHtml(String filename, Object... params) {
        InputStream inputStream = ResourcesUtils.get("html" + File.separator + filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, UTF_8));
        String line;
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                if ((line = reader.readLine()) == null) {
                    break;
                }
                sb.append(line);
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
        if (params.length == 0) {
            return sb.toString();
        }
        return String.format(sb.toString(), params);
    }
}
