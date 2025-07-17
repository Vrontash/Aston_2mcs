package org.example.warehouseservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.warehouseservice.dto.OrderDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaOrdersEventListener {
    private final WarehouseService warehouseService;

    @KafkaListener( topics = "#{@kafkaProperties.topics.orderCreated.name}",
            groupId = "#{@kafkaProperties.consumer.groupId}")
    public void orderHandler(ConsumerRecord<String, OrderDto> record) {
        String key = record.key();
        OrderDto orderDto = record.value();
        log.info("Successfully parsed order with key: {} and value: {}", key, orderDto);
        warehouseService.checkSingleOrderForStatusChange(orderDto, warehouseService.findAllItemsQuantity());
    }
}
