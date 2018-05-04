package com.hnqc.coremember.spider;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DbUnit
 *
 * @author zido
 * @date 2018/04/23
 */
public class DbUnit {
    private static Logger logger = LoggerFactory.getLogger(DbUnit.class);
    private static DbUnit instance = null;
    private static DataSource dataSource = null;

    static {
        Properties properties = loadPropertyFile("db.properties");
        try {
            dataSource = DruidDataSourceFactory
                    .createDataSource(properties);
            //程序关闭时自动释放连接
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                ((DruidDataSource) dataSource).close();
                logger.info("回收数据库连接");
            }));
        } catch (Exception e) {
            logger.error("druid datasource create fail.", e);
            throw new RuntimeException(e);
        }
    }

    private DbUnit() {
    }

    public static DbUnit getInstance() {
        if (instance == null) {
            synchronized (DbUnit.class) {
                if (instance == null) {
                    instance = new DbUnit();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private static Properties loadPropertyFile(String path) {
        Properties p = new Properties();
        InputStream inStream = DbUnit.class.getClassLoader().getResourceAsStream(path);
        try {
            p.load(inStream);
        } catch (IOException e) {
            logger.error("mysql configuration load fail.", e);
            System.exit(1);
        }
        return p;
    }
}
