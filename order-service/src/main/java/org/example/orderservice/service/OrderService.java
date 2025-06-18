package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.config.KafkaProperties;
import org.example.orderservice.dto.OrderDto;
import org.example.orderservice.exception.OrderNotFoundException;
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

    public OrderDto createOrder(OrderDto orderDto) {
        OrderDto newOrderDto = entityToDto(orderRepository.save(dtoToEntity(orderDto)));
        kafkaSender.send(kafkaProperties.getTopics().getOrderCreated().getName(),
                String.valueOf(orderDto.getCustomerId()),
                newOrderDto);
        log.info("Order created : {}", newOrderDto);
        return newOrderDto;
    }

    public OrderDto findOrder(int orderId) {
        log.info("Got order with Id: {}", orderId);
        return orderRepository.findById(orderId)
                .map(this::entityToDto)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }
    public List<OrderDto> findOrdersByStatus(OrderStatus orderStatus) {
        return orderRepository.findByStatus(orderStatus).stream()
                .map(this::entityToDto)
                .toList();
    }
    public List<OrderDto> findAllOrders() {
        log.info("Retrieved all orders");
        return orderRepository.findAll()
                .stream()
                .map(this::entityToDto)
                .toList();
    }

    public OrderDto updateOrder(OrderDto orderDto) {
        Order order = orderRepository.findById(orderDto.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(orderDto.getOrderId()));
        order.setItemId(orderDto.getItemId());
        order.setCustomerId(orderDto.getCustomerId());
        order.setPrice(orderDto.getPrice());
        order.setQuantity(orderDto.getQuantity());
        order.setStatus(orderDto.getStatus());
        log.info("Order with Id: {} updated", orderDto.getOrderId());
        return entityToDto(orderRepository.save(order));
    }
    public void deleteOrder(int id) {
        orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        orderRepository.deleteById(id);
        log.info("Order with Id: {} deleted", id);
    }

    private Order dtoToEntity(OrderDto orderDto) {
        Order order = new Order();
        order.setCustomerId(orderDto.getCustomerId());
        order.setItemId(orderDto.getItemId());
        order.setQuantity(orderDto.getQuantity());
        order.setPrice(orderDto.getPrice());
        order.setStatus(orderDto.getStatus());
        return order;
    }

    private OrderDto entityToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getOrderId());
        orderDto.setCustomerId(order.getCustomerId());
        orderDto.setItemId(order.getItemId());
        orderDto.setQuantity(order.getQuantity());
        orderDto.setPrice(order.getPrice());
        orderDto.setStatus(order.getStatus());
        return orderDto;
    }

}
