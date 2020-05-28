package site.zido.elise.task.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import site.zido.elise.select.XpathSelector;
import site.zido.elise.task.model.Model;

import java.io.*;

public class DefaultSelectableResponseTest {
    @Test
    public void testBuildModel() throws IOException {
        DefaultSelectableResponse response = new DefaultSelectableResponse();
        response.modelName("test_model");
        response.target().matchStatusCode("200<300").and().matchUrl("http://xxx.yyy");
        response.target().matchUrl("http://aaa.bbb");
        response.helper().regex("ddd$").and().regex("^aaa").or().regex("^ccc");
        response.content().statusCode().nullable(false).name("code");
        response.content().url().nullable(false).name("url");
        PartitionDescriptor partition = response.partition(new XpathSelector("//div[@class='profile']"));
        partition.field().css(".text").rich().name("content").nullable(true);
        partition.field().xpath(".description").text().name("description");
        response.content().html().css(".author").text().name("author").nullable(false);
        Model model = response.getModel();

        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader().getResourceAsStream("task" + File.separator + "api" + File.separator + "model1.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line.trim());
        }
        //格式化一次
        Assert.assertEquals(mapper.writeValueAsString(mapper.readTree(builder.toString())), mapper.writeValueAsString(model));
    }
}
