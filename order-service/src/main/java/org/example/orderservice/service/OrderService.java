package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.config.KafkaProperties;
import org.example.orderservice.dto.OrderDto;
import org.example.orderservice.exception.OrderNotFoundException;
import org.example.orderservice.mapper.OrderMapper;
import org.example.orderservice.model.Order;
import org.example.orderservice.model.OrderStatus;
import org.example.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;


import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final KafkaSender<OrderDto> kafkaSender;
    private final OrderRepository orderRepository;
    private final KafkaProperties kafkaProperties;
    private final OrderMapper orderMapper;

    public OrderDto createOrder(OrderDto orderDto) {
        OrderDto newOrderDto = orderMapper.toOrderDto(orderRepository.save(orderMapper.toOrder(orderDto)));
        kafkaSender.send(kafkaProperties.getTopics().getOrderCreated().getName(),
                String.valueOf(orderDto.getCustomerId()),
                newOrderDto);
        log.info("Order created : {}", newOrderDto);
        return newOrderDto;
    }

    public OrderDto findOrder(int orderId) {
        log.info("Got order with Id: {}", orderId);
        return orderRepository.findById(orderId)
                .map(orderMapper::toOrderDto)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }
    public List<OrderDto> findOrdersByStatus(OrderStatus orderStatus) {
        return orderRepository.findByStatus(orderStatus).stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }
    public List<OrderDto> findAllOrders() {
        log.info("Retrieved all orders");
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    public OrderDto updateOrder(OrderDto orderDto) {
        Order order = orderRepository.findById(orderDto.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(orderDto.getOrderId()));
        orderMapper.updateOrderFromDto(orderDto, order);
        log.info("Order with Id: {} updated", orderDto.getOrderId());
        return orderMapper.toOrderDto(orderRepository.save(order));
    }
    public void deleteOrder(int id) {
        orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        orderRepository.deleteById(id);
        log.info("Order with Id: {} deleted", id);
    }
}