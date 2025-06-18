package org.example.orderservice.service;

public  interface  KafkaSender<T>{
    void send(String topic, T message);
    void send(String topic, String key, T message);
}
