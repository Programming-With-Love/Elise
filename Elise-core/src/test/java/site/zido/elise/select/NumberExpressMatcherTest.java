package site.zido.elise.select;

import org.junit.Assert;
import org.junit.Test;

public class NumberExpressMatcherTest {
    @Test
    public void testCompile(){
        try{
            new NumberExpressMatcher("daw3484");
        }catch (IllegalArgumentException e){
            Assert.assertNotNull(e);
            Assert.assertEquals("express only can contains [0-9,<-]",e.getMessage());
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
        Assert.assertTrue(matcher.matches(-4));
        Assert.assertTrue(matcher.matches(5));
        Assert.assertFalse(matcher.matches(3));
        matcher = new NumberExpressMatcher("<-3,5,6<");
        Assert.assertTrue(matcher.matches(-5));
        Assert.assertFalse(matcher.matches(4));
        Assert.assertTrue(matcher.matches(6));
        Assert.assertTrue(matcher.matches(Integer.MAX_VALUE));
    }
}
