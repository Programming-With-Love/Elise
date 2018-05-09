package site.zido.elise.spider.service;

import com.hnqc.coremember.spider.DbUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MemberService
 *
 * @author zido
 * @date 2018/05/04
 */
@Service
public class MemberService {
    private Logger logger = LoggerFactory.getLogger(MemberService.class);

    public List<String> findMemberNames() {
        try (Connection connection = DbUnit.getInstance().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("select name from hnqc_core_member.member");
            List<String> names = new ArrayList<>();
            while (set.next()) {
                String name = set.getString(1);
                names.add(name);
            }
            return names;
        } catch (SQLException e) {
            logger.error("get connect error", e);
        }
        return new ArrayList<>();
    }

    public List<String> findKeyWords() {
        try (Connection connection = DbUnit.getInstance().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("select text from hnqc_core_member.high_word");
            List<String> names = new ArrayList<>();
            while (set.next()) {
                String name = set.getString(1);
                names.add(name);
            }
            return names;
        } catch (SQLException e) {
            logger.error("get connect error", e);
        }
        return new ArrayList<>();
    }

    public void moveToSpiderByName(String name) {
        try (Connection connection = DbUnit.getInstance().getConnection()) {
            String sql = "insert into hnqc_core_member.member_spider (id,member_name,create_time,text,origin,url) " +
                    "select id,? as member_name,create_time,text,origin,url from hnqc_core_member.member_spider_tmp " +
                    "where member_spider_tmp.id not in (select id from hnqc_core_member.member_spider) " +
                    "and title like ? or text like ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, "%" + name + "%");
            preparedStatement.setString(3, "%" + name + "%");
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error("get connect error", e);
        }
    }

    public void moveToSpiderByKeyWord(List<String> keyWord, String targetName) {
        try (Connection connection = DbUnit.getInstance().getConnection()) {
            StringBuilder sql = new StringBuilder("insert into hnqc_core_member.member_spider (id,member_name,create_time,text,origin,url) " +
                    "select id,? as member_name,create_time,text,origin,url from hnqc_core_member.member_spider_tmp " +
                    "where member_spider_tmp.id not in (select id from hnqc_core_member.member_spider) ");
            for (int i = 0; i < keyWord.size(); i++) {
                String subSql = "and title like ? or text like ?";
                sql.append(subSql);
            }
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
            preparedStatement.setString(1, targetName);
            for (int i = 0; i < keyWord.size(); i++) {
                preparedStatement.setString(2 * (i + 1), "%" + keyWord.get(i) + "%");
                preparedStatement.setString(2 * (i + 1) + 1, "%" + keyWord.get(i) + "%");
            }
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error("get connect error", e);
        }
    }
}
