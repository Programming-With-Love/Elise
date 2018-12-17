package site.zido.elise.task.api;

import org.junit.Assert;
import org.junit.Test;
import site.zido.elise.select.XpathSelector;
import site.zido.elise.task.model.Model;

public class DefaultSelectableResponseTest {
    @Test
    public void testBuildModel() {
        DefaultSelectableResponse response = new DefaultSelectableResponse();
        response.modelName("test_model");
        response.asTarget().matchStatusCode("200<300").and().matchUrl("http://xxx.yyy");
        response.asTarget().matchUrl("http://aaa.bbb");
        response.asHelper().regex("ddd$").and().regex("^aaa").or().regex("^ccc");
        response.asContent().statusCode().nullable(false).save("code");
        response.asContent().url().nullable(false).save("url");
        ElementSelectable partition = response.asContent().html().partition(new XpathSelector("//div[@class='profile']"));
        partition.css(".text").rich().save("content").nullable(true);
        partition.xpath(".description").text().save("description");
        response.asContent().html().css(".author").text().save("author").nullable(false);
        Model model = response.getModel();

        Assert.assertEquals("test_model",model.getName());
        Assert.assertEquals(2,model.getTargets().size());
    }
}
