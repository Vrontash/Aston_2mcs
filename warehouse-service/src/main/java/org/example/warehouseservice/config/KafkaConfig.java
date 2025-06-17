package org.example.warehouseservice.config;


import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;


import org.apache.kafka.clients.producer.ProducerConfig;

import org.example.warehouseservice.dto.OrderDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;


import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
    final KafkaProperties kafkaProperties;
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getProducer().getBootstrapServer());
        return new KafkaAdmin(configs);
    }
    @Bean
    public ConsumerFactory<String, OrderDto> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getConsumer().getBootstrapServer());
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumer().getGroupId());
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getConsumer().getKeyDeserializer());
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getConsumer().getValueDeserializer());
        configProps.putAll(kafkaProperties.getConsumer().getAdditionalProperties());

        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderDto> kafkaListenerContainerFactory(KafkaTemplate<String, Object> kafkaTemplate) {
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                                                new DeadLetterPublishingRecoverer(kafkaTemplate),
                                                new FixedBackOff(
                                                        kafkaProperties.getConsumer().getFixedBackOffInterval(),
                                                        kafkaProperties.getConsumer().getFixedBackOffMaxAttempts()
                                                )
        );
        ConcurrentKafkaListenerContainerFactory<String, OrderDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getProducer().getBootstrapServer());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProperties.getProducer().getKeySerializer());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.getProducer().getValueSerializer());
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public NewTopic createOrderDtlTopic() {
        return TopicBuilder.name(kafkaProperties.getTopics().getOrderCreatedDlt().getName())
                .partitions(kafkaProperties.getTopics().getOrderCreatedDlt().getPartitions())
                .replicas(kafkaProperties.getTopics().getOrderCreatedDlt().getReplicas())
                .build();
    }
}
