package com.hnqc.ironhand.spider.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DbConfig {

    private String driver;
    private String url;
    private String userName;
    private String password;

    public DbConfig() {
        this("db.properties");
    }

    public DbConfig(String path) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
        Properties p = new Properties();
        try {
            p.load(inputStream);
            this.driver = p.getProperty("driver");
            this.url = p.getProperty("url");
            this.userName = p.getProperty("username");
            this.password = p.getProperty("password");
        } catch (IOException e) {
            throw new RuntimeException("jdbc connect failed");
        }

    }

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }


}
