package org.example.orderservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.exception.KafkaMessageNotSendException;
import org.example.orderservice.service.KafkaSender;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaSenderImpl implements KafkaSender {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(String topic, Object message) {
        try {
            kafkaTemplate.send(topic, message);
            log.info("Kafka: Message sent to topic: {} with message: {}", topic, message);
        } catch (Exception e) {
            log.error("Kafka: Failed to send message to topic: {} with message: {}", topic, message, e);
            throw new KafkaMessageNotSendException("Failed to send message to topic: " + topic, e);
        }
    }

    @Override
    public void send(String topic, String key, Object message) {
        try {
            kafkaTemplate.send(topic, key,  message);
            log.info("Kafka: Message sent to topic: {} with key: {} and message: {}", topic, key, message);
        } catch (Exception e) {
            log.error("Kafka: Message sent to topic: {} with key: {} and message: {}", topic, key, message, e);
            throw new KafkaMessageNotSendException("Failed to send message to topic: " + topic, e);
        }
    }
}
