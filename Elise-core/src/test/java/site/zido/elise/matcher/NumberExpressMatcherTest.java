package site.zido.elise.matcher;

import org.junit.Assert;
import org.junit.Test;
import site.zido.elise.utils.Asserts;

public class NumberExpressMatcherTest {
    @Test
    public void testCompile(){
        try{
            new NumberExpressMatcher("daw3484");
        }catch (IllegalArgumentException e){
            Assert.assertNotNull(e);
            Assert.assertEquals("express only can contains [1-9,<-]",e.getMessage());
        }
        try{
            new NumberExpressMatcher(",,12");
        }catch (IllegalArgumentException e){
            Assert.assertNotNull(e);
        }

        NumberExpressMatcher matcher = new NumberExpressMatcher("1<2,3<");
        Assert.assertTrue(matcher.matches(12));
        Assert.assertFalse(matcher.matches(0));
        Assert.assertFalse(matcher.matches("12"));

        matcher = new NumberExpressMatcher("1,5,8,-3<-5");
    }
}
