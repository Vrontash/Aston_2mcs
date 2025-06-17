package org.example.orderservice.service;

public interface KafkaSender{
    void send(String topic, Object message);
    void send(String topic, String key, Object message);
}
