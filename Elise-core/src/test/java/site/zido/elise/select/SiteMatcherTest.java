package site.zido.elise.select;

import org.junit.Assert;
import org.junit.Test;
import site.zido.elise.select.matcher.SiteMatcher;

public class SiteMatcherTest {
    @Test
    public void testMatches() {
        SiteMatcher matcher = new SiteMatcher("*abc");
        Assert.assertFalse(matcher.matches("abcdab"));
        Assert.assertTrue(matcher.matches("abcdabc"));
        matcher = new SiteMatcher("ab*abc*bc");
        Assert.assertTrue(matcher.matches("abcdwfaabcfergerhgabc"));
        Assert.assertFalse(matcher.matches("abdwdfabcawdbbb"));
    }
}
