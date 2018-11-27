package site.zido.elise.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class HtmlUtilsTest {
    private static final String[] charsets = {"UTF-8", "GB2312", "GBK"};

    @Test
    public void testGetCharset() throws UnsupportedEncodingException {
        for (String currentCharset : charsets) {
            String html = "<html><meta charset=\"" + currentCharset + "\"/><body></body></html>";
            String charset = HtmlUtils.getHtmlCharset(html.getBytes(currentCharset));
            Assert.assertEquals(currentCharset, charset);
        }
    }

    @Test
    public void testGetHtmlHead() {
        String head = "<head  > adwadefe</head >";
        String html = "<html>" + head + "<body> dwfrewg</body></html>";
        String headStr = HtmlUtils.getHeadStr(html.toCharArray());
        Assert.assertEquals(head, headStr);
    }
}
