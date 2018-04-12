package com.hnqc.ironhand.common.broadcast;

import com.hnqc.ironhand.common.CommonConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MessageConfiguration {

    private CommonConfig commonConfig;
    private BroadcastListener.Handler handler;
    @Value("${com.hnqc.config.message-service}")
    private String service;

    @Autowired
    public MessageConfiguration(CommonConfig commonConfig, BroadcastListener.Handler handler) {
        this.commonConfig = commonConfig;
        this.handler = handler;
    }

    /**
     * 获取实例
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, Object>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

    private ConsumerFactory<Integer, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    private Map<String, Object> consumerConfig() {
        Map<String, Object> props = new HashMap<>(7);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, service);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.hnqc.ironhand.common.pojo.message,com.hnqc.ironhand.common.pojo.entity");
        return props;
    }

    @Bean("broadcastListener")
    public BroadcastListener listener() {
        return new BroadcastListener(commonConfig.getBroadId(), handler);
    }

    private Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>(7);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, service);
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    private ProducerFactory<Integer, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }


    @Bean
    public KafkaTemplate<Integer, Object> template() {
        return new KafkaTemplate<>(producerFactory());
    }
}
