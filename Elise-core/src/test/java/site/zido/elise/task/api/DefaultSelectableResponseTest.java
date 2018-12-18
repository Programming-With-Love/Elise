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
        response.asTarget().matchStatusCode("200<300").and().matchUrl("http://xxx.yyy");
        response.asTarget().matchUrl("http://aaa.bbb");
        response.asHelper().regex("ddd$").and().regex("^aaa").or().regex("^ccc");
        response.asContent().statusCode().nullable(false).save("code");
        response.asContent().url().nullable(false).save("url");
        PartitionDescriptor partition = response.asPartition(new XpathSelector("//div[@class='profile']"));
        partition.field().css(".text").rich().save("content").nullable(true);
        partition.field().xpath(".description").text().save("description");
        response.asContent().html().css(".author").text().save("author").nullable(false);
        Model model = response.getModel();

        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader().getResourceAsStream("task" + File.separator + "api" + File.separator + "model1.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line.trim());
        }
        Assert.assertEquals(builder.toString(), mapper.writeValueAsString(model));
    }
}
