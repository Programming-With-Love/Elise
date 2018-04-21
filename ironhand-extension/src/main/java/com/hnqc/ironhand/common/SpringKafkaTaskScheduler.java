package com.hnqc.ironhand.common;

import com.hnqc.ironhand.DistributedTask;
import com.hnqc.ironhand.Page;
import com.hnqc.ironhand.Request;
import com.hnqc.ironhand.Task;
import com.hnqc.ironhand.common.pojo.Seed;
import com.hnqc.ironhand.scheduler.*;
import com.hnqc.ironhand.utils.IdWorker;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Spring Kafka Communication Manager
 *
 * @author zido
 * @date 2018/04/19
 */
public class SpringKafkaTaskScheduler extends AbstractDuplicateRemovedScheduler {
    private String bootstrapServers;
    private String groupId = "ironhand";

    private KafkaTemplate<Long, Seed> template;

    private TaskScheduler taskScheduler;

    private KafkaMessageListenerContainer<Long, Seed> analyzerContainer;
    private KafkaMessageListenerContainer<Long, Seed> downloaderContainer;

    public SpringKafkaTaskScheduler(TaskScheduler scheduler, DuplicationProcessor duplicationProcessor) {
        super(duplicationProcessor);
        this.taskScheduler = scheduler;
    }

    public SpringKafkaTaskScheduler(DuplicationProcessor duplicationProcessor) {
        this(new SimpleTaskScheduler(), duplicationProcessor);
    }

    public SpringKafkaTaskScheduler setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
        this.template = createTemplate();
        return this;
    }

    public SpringKafkaTaskScheduler setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    @Override
    public synchronized void registerAnalyzer(AnalyzerListener listener) {
        taskScheduler.registerAnalyzer(listener);
        if (this.analyzerContainer == null) {
            this.analyzerContainer = runContainer(TYPE_MESSAGE_ANALYZER, message -> {
                Seed seed = message.value();
                taskScheduler.process(seed.getTask(), seed.getRequest(), seed.getPage());
            });
        }
        if(!this.analyzerContainer.isRunning()){
            this.analyzerContainer.start();
        }
    }

    @Override
    public synchronized void registerDownloader(DownloadListener listener) {
        taskScheduler.registerDownloader(listener);
        if (this.downloaderContainer == null) {
            this.downloaderContainer = runContainer(TYPE_MESSAGE_DOWNLOAD, message -> {
                Seed seed = message.value();
                taskScheduler.pushRequest(seed.getTask(), seed.getRequest());
            });
        }
        if(!this.downloaderContainer.isRunning()){
            this.downloaderContainer.start();
        }

    }

    @Override
    public synchronized void removeAnalyzer(AnalyzerListener listener) {
        taskScheduler.removeAnalyzer(listener);
        if (taskScheduler instanceof MonitorableScheduler) {
            MonitorableScheduler monitorableScheduler = (MonitorableScheduler) taskScheduler;
            if (monitorableScheduler.analyzerSize() <= 0) {
                this.analyzerContainer.stop();
            }
        }
    }

    @Override
    public synchronized void removeDownloader(DownloadListener listener) {
        taskScheduler.removeDownloader(listener);
        if (taskScheduler instanceof MonitorableScheduler) {
            MonitorableScheduler monitorableScheduler = (MonitorableScheduler) taskScheduler;
            if (monitorableScheduler.downloaderSize() <= 0) {
                this.downloaderContainer.stop();
            }
        }
    }

    private KafkaMessageListenerContainer<Long, Seed> runContainer(String type, MessageListener<Long, Seed> listener) {
        ContainerProperties containerProps = new ContainerProperties(type);
        containerProps.setMessageListener(listener);
        KafkaMessageListenerContainer<Long, Seed> tmp = createContainer(containerProps);
        tmp.setBeanName(type + "message-listener");
        return tmp;
    }

    @Override
    public void process(Task task, Request request, Page page) {
        template.send(TYPE_MESSAGE_ANALYZER, new Seed().setTask((DistributedTask) task).setRequest(request).setPage(page));
    }

    @Override
    protected void pushWhenNoDuplicate(Request request, Task task) {
        template.send(TYPE_MESSAGE_DOWNLOAD, new Seed().setTask((DistributedTask) task).setRequest(request));
    }

    private KafkaMessageListenerContainer<Long, Seed> createContainer(
            ContainerProperties containerProps) {
        Map<String, Object> props = consumerProps();
        DefaultKafkaConsumerFactory<Long, Seed> cf =
                new DefaultKafkaConsumerFactory<>(props);
        return new KafkaMessageListenerContainer<>(cf, containerProps);
    }

    private KafkaTemplate<Long, Seed> createTemplate() {
        Map<String, Object> senderProps = senderProps();
        ProducerFactory<Long, Seed> pf =
                new DefaultKafkaProducerFactory<>(senderProps);
        return new KafkaTemplate<>(pf, true);
    }

    private Map<String, Object> consumerProps() {
        if (bootstrapServers == null) {
            throw new IllegalArgumentException("bootstrapServers can not be null");
        }
        Map<String, Object> props = new HashMap<>(7);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.hnqc.ironhand.common.pojo");
        return props;
    }

    private Map<String, Object> senderProps() {
        if (bootstrapServers == null) {
            throw new IllegalArgumentException("bootstrapServers can not be null");
        }
        Map<String, Object> props = new HashMap<>(7);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }
}
