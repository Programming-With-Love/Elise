package com.hnqc.ironhand.common;

import com.hnqc.ironhand.*;
import com.hnqc.ironhand.configurable.ConfigurableUrlFinder;
import com.hnqc.ironhand.configurable.DefRootExtractor;
import com.hnqc.ironhand.scheduler.DuplicationProcessor;
import com.hnqc.ironhand.scheduler.HashSetDeduplicationProcessor;
import com.hnqc.ironhand.selector.PlainText;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * KafkaTaskSchedulerTest
 *
 * @author zido
 * @date 2018/04/21
 */
public class KafkaTaskSchedulerTest {
    private static Logger logger = LoggerFactory.getLogger(KafkaTaskSchedulerTest.class);

    @Test
    public void testKafka() throws InterruptedException {
        logger.info("Start auto");
        ContainerProperties containerProps = new ContainerProperties("topic1", "topic2");
        final CountDownLatch latch = new CountDownLatch(4);
        containerProps.setMessageListener((MessageListener<Integer, String>) message -> {
            logger.info("received: " + message.value());
            latch.countDown();
        });
        KafkaMessageListenerContainer<Integer, String> container = createContainer(containerProps);
        container.setBeanName("testAuto");
        container.start();
        Thread.sleep(1000); // wait a bit for the container to start
        KafkaTemplate<Integer, String> template = createTemplate();
        template.setDefaultTopic("topic1");
        template.sendDefault(0, "foo");
        template.sendDefault(2, "bar");
        template.sendDefault(0, "baz");
        template.sendDefault(2, "qux");
        template.flush();
        Assert.assertTrue(latch.await(60, TimeUnit.SECONDS));
        container.stop();
        logger.info("Stop auto");
    }


    @Test
    public void testScheduler() throws InterruptedException {
        DistributedTask dTask = new DistributedTask(6L, new Site().setCycleRetryTimes(10),
                new DefRootExtractor()
                        .addTargetUrl(new ConfigurableUrlFinder()
                                .setType(ConfigurableUrlFinder.Type.REGEX)
                                .setValue(".*")));

        CountDownLatch latch = new CountDownLatch(6);
        SpringKafkaTaskScheduler scheduler = new SpringKafkaTaskScheduler(new HashSetDeduplicationProcessor());
        scheduler.setBootstrapServers("192.168.0.103:9092");
        scheduler.registerAnalyzer((task, request, page) -> {
            DistributedTask tmp = (DistributedTask) task;
            logger.info("analyzer message:" + task.getId());
            Assert.assertEquals(".*", tmp.getDefExtractor().getTargetUrl().get(0).getValue());
            latch.countDown();
        });
        scheduler.registerDownloader(((task, request) -> {
            DistributedTask tmp = (DistributedTask) task;
            logger.info("downloader message:" + task.getId());
            Assert.assertEquals(".*", tmp.getDefExtractor().getTargetUrl().get(0).getValue());
            latch.countDown();
        }));
        // wait a bit for the container to start
        Thread.sleep(3000);

        scheduler.pushRequest(dTask, new Request("www.baidu.com"));
        scheduler.pushRequest(dTask, new Request("www.sohu.com"));
        scheduler.process(dTask, new Request("www.baidu.com"), new Page().setUrl(new PlainText("www.baidu.com")).setRawText("<div/>"));
        scheduler.process(dTask.setId(7L), new Request("www.baidu.com"), new Page().setUrl(new PlainText("www.baidu.com")).setRawText("<div/>"));
        scheduler.process(dTask.setId(8L), new Request("www.baidu.com"), new Page().setUrl(new PlainText("www.baidu.com")).setRawText("<div/>"));
        scheduler.process(dTask.setId(9L), new Request("www.baidu.com"), new Page().setUrl(new PlainText("www.baidu.com")).setRawText("<div/>"));
        Assert.assertTrue(latch.await(20, TimeUnit.SECONDS));
        Assert.assertEquals(0, latch.getCount());
    }

    private KafkaMessageListenerContainer<Integer, String> createContainer(
            ContainerProperties containerProps) {
        Map<String, Object> props = consumerProps();
        DefaultKafkaConsumerFactory<Integer, String> cf =
                new DefaultKafkaConsumerFactory<>(props);
        KafkaMessageListenerContainer<Integer, String> container =
                new KafkaMessageListenerContainer<>(cf, containerProps);
        return container;
    }

    private KafkaTemplate<Integer, String> createTemplate() {
        Map<String, Object> senderProps = senderProps();
        ProducerFactory<Integer, String> pf =
                new DefaultKafkaProducerFactory<>(senderProps);
        KafkaTemplate<Integer, String> template = new KafkaTemplate<>(pf);
        return template;
    }

    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.103:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    private Map<String, Object> senderProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.103:9092");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }
}
