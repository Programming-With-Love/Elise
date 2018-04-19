package com.hnqc.ironhand.common;

import com.hnqc.ironhand.common.pojo.Seed;
import com.hnqc.ironhand.Page;
import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Task;
import com.hnqc.ironhand.message.CommunicationManager;
import com.hnqc.ironhand.message.MonitorableContainer;
import com.hnqc.ironhand.message.SimpleCommunicationManager;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spring Kafka Communication Manager
 *
 * @author zido
 * @date 2018/04/19
 */
public class SpringKafkaCommunicationManager implements CommunicationManager {
    private KafkaTemplate<Integer, Seed> template;

    private CommunicationManager communicationManager;

    private Status status = Status.Init;

    private Map<String, KafkaMessageListenerContainer<Integer, Seed>> container = new ConcurrentHashMap<>();

    public SpringKafkaCommunicationManager() {
        this(new SimpleCommunicationManager());
    }

    public SpringKafkaCommunicationManager(CommunicationManager communicationManager) {
        if (!(communicationManager instanceof MonitorableContainer)) {
            throw new UnsupportedOperationException("unmonitorable manager is not supported");
        }
        this.communicationManager = communicationManager;
    }

    @Override
    public void registerAnalyzer(AnalyzerListener listener) {
        communicationManager.registerAnalyzer(listener);
    }

    @Override
    public void registerDownloader(DownloadListener listener) {
        communicationManager.registerDownloader(listener);
    }

    @Override
    public void removeAnalyzer(AnalyzerListener listener) {
        communicationManager.removeAnalyzer(listener);
    }

    @Override
    public void removeDownloader(DownloadListener listener) {
        communicationManager.removeDownloader(listener);
    }

    private KafkaMessageListenerContainer<Integer, Seed> runContainer(String type, MessageListener<Integer, Seed> listener) {
        ContainerProperties containerProps = new ContainerProperties(TYPE_MESSAGE_DOWNLOAD);
        containerProps.setMessageListener(listener);
        KafkaMessageListenerContainer<Integer, Seed> tmp = createContainer(containerProps);
        tmp.setBeanName(type + "-message-listener");
        tmp.start();
        return tmp;
    }

    @Override
    public void start() {
        this.status = Status.Init;
        this.communicationManager.start();
        MonitorableContainer monitorableContainer = (MonitorableContainer) communicationManager;
        if (monitorableContainer.size(TYPE_MESSAGE_DOWNLOAD) > 0 && this.container.get(TYPE_MESSAGE_DOWNLOAD) == null) {
            this.container.put(TYPE_MESSAGE_DOWNLOAD, runContainer(TYPE_MESSAGE_DOWNLOAD, message -> {
                Seed seed = message.value();
                communicationManager.download(seed.getTask(), seed.getRequest());
            }));
        }
        if (monitorableContainer.size(TYPE_MESSAGE_ANALYZER) > 0 && this.container.get(TYPE_MESSAGE_ANALYZER) == null) {
            this.container.put(TYPE_MESSAGE_ANALYZER, runContainer(TYPE_MESSAGE_ANALYZER, message -> {
                Seed seed = message.value();
                communicationManager.process(seed.getTask(), seed.getRequest(), seed.getPage());
            }));
        }
        if (this.template == null) {
            this.template = createTemplate();
        }
        this.status = Status.Running;
    }

    @Override
    public void process(Task task, Request request, Page page) {
        template.send("analyzer", new Seed().setTask(task).setRequest(request).setPage(page));
    }

    @Override
    public void download(Task task, Request request) {
        template.send("download", new Seed().setTask(task).setRequest(request));
    }

    @Override
    public void stop() {
        this.template.flush();
        KafkaMessageListenerContainer<Integer, Seed> analyzerListenerContainer = this.container.get(TYPE_MESSAGE_ANALYZER);
        analyzerListenerContainer.stop();

        KafkaMessageListenerContainer<Integer, Seed> downloaderListenerContainer = this.container.get(TYPE_MESSAGE_DOWNLOAD);
        downloaderListenerContainer.stop();
        this.communicationManager.stop();
        this.status = Status.Stopped;
    }

    @Override
    public Status getStatus() {
        return this.status;
    }

    private KafkaMessageListenerContainer<Integer, Seed> createContainer(
            ContainerProperties containerProps) {
        Map<String, Object> props = consumerProps();
        DefaultKafkaConsumerFactory<Integer, Seed> cf =
                new DefaultKafkaConsumerFactory<>(props);
        return new KafkaMessageListenerContainer<>(cf, containerProps);
    }

    private KafkaTemplate<Integer, Seed> createTemplate() {
        Map<String, Object> senderProps = senderProps();
        ProducerFactory<Integer, Seed> pf =
                new DefaultKafkaProducerFactory<>(senderProps);
        return new KafkaTemplate<>(pf);
    }

    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>(7);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "101.132.114.206:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "ironhand");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    private Map<String, Object> senderProps() {
        Map<String, Object> props = new HashMap<>(7);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "101.132.114.206:9092");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }
}
