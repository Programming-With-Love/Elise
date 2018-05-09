package site.zido.elise.spider.service;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * MemberServiceTest
 *
 * @author zido
 * @date 2018/05/04
 */
public class MemberServiceTest {
    @Test
    public void testFindNames() {
        MemberService memberService = new MemberService();
        List<String> memberNames = memberService.findMemberNames();
        Assert.assertTrue(memberNames.size() > 0);
    }

    @Test
    public void testMoveToSpiderByName(){
        MemberService memberService = new MemberService();
        memberService.moveToSpiderByName("石蓉");
    }

    @Test
    public void testMoveToSpiderByKeyWord(){
        MemberService service = new MemberService();
        service.moveToSpiderByKeyWord(Arrays.asList("四川大学","锦城学院"),"陌生校友");
    }
}
