package com.hnqc.ironhand;

import com.hnqc.ironhand.common.SimpleRedisDuplicationProcessor;
import com.hnqc.ironhand.common.SpringKafkaTaskScheduler;
import com.hnqc.ironhand.pipeline.AbstractSqlPipeline;
import com.hnqc.ironhand.processor.ExtractorPageProcessor;
import com.hnqc.ironhand.scheduler.NoDepuplicationProcessor;
import com.hnqc.ironhand.scheduler.SimpleTaskScheduler;
import com.hnqc.ironhand.utils.IdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

/**
 * AnalyzerClient
 *
 * @author zido
 * @date 2018/04/22
 */
public class AnalyzerClient {
    private final Spider spider;
    private Logger logger = LoggerFactory.getLogger(AnalyzerClient.class);

    private final static String REDIS_URL = "redisUrl";
    private final static String KAFKA_SERVERS = "kafkaServers";

    public AnalyzerClient() {
        Properties properties = new Properties();
        try {
            InputStream stream = this.getClass().getResourceAsStream("/config.properties");
            if (stream == null) {
                throw new RuntimeException("config.properties load failed");
            }
            properties.load(stream);
        } catch (IOException e) {
            throw new RuntimeException("加载客户端配置信息失败", e);
        }
        SpringKafkaTaskScheduler scheduler = new SpringKafkaTaskScheduler(
                new SimpleTaskScheduler(new NoDepuplicationProcessor()).setPoolSize(10),
                new SimpleRedisDuplicationProcessor(properties.getProperty(REDIS_URL)))
                .setBootstrapServers(properties.getProperty(KAFKA_SERVERS))
                .setReadListener(new OssReadListener());
        spider = new Spider(
                scheduler,
                new ExtractorPageProcessor(),
                null,
                new AbstractSqlPipeline() {
                    @Override
                    public void onInsert(String sql, Object[] object) {
                        try {
                            DbUnit.getInstance().insert(sql, object);
                        } catch (SQLException e) {
                            logger.error("插入到数据库时发生异常，sql语句：" + sql, e);
                        }
                    }
                }.setGenerator(IdWorker::nextId));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("停止kafka消息监听容器");
            scheduler.stop();
        }));
    }

    public void start() {
        this.spider.start();
    }

    public static void main(String[] args) {
        new AnalyzerClient().start();
    }
}
