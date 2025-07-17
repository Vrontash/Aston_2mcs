package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.config.properties.KafkaProperties;
import org.example.orderservice.dto.OrderDto;
import org.example.orderservice.exception.OrderNotFoundException;
import org.example.orderservice.mapper.OrderMapper;
import org.example.orderservice.model.Order;
import org.example.orderservice.model.OrderStatus;
import org.example.orderservice.repository.OrderDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.SQLException;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final KafkaSender<OrderDto> kafkaSender;
    private final OrderDao orderDao;
    private final KafkaProperties kafkaProperties;
    private final OrderMapper orderMapper;

    @Transactional(rollbackFor = {SQLException.class, RuntimeException.class})
    public OrderDto createOrder(OrderDto orderDto) {
        OrderDto newOrderDto = orderMapper.toOrderDto(orderDao.save(orderMapper.toOrder(orderDto)));
        kafkaSender.send(kafkaProperties.getTopics().getOrderCreated().getName(),
                String.valueOf(newOrderDto.getOrderId()),
                newOrderDto);
        log.info("Order created : {}", newOrderDto);
        return newOrderDto;
    }

    @Transactional(readOnly = true)
    public OrderDto findOrder(int orderId) {
        log.info("Got order with Id: {}", orderId);
        return orderDao.findById(orderId)
                .map(orderMapper::toOrderDto)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findOrdersByStatus(OrderStatus orderStatus) {
        return orderDao.findByStatus(orderStatus).stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findAllOrders() {
        log.info("Retrieved all orders");
        return orderDao.findAll()
                .stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    @Transactional(rollbackFor = {SQLException.class, RuntimeException.class})
    public OrderDto updateOrder(OrderDto orderDto) {
        Order order = orderDao.findById(orderDto.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(orderDto.getOrderId()));
        orderMapper.updateOrderFromDto(orderDto, order);
        log.info("Order with Id: {} updated", orderDto.getOrderId());
        return orderMapper.toOrderDto(orderDao.update(order));
    }

    @Transactional(rollbackFor = {SQLException.class, RuntimeException.class})
    public void deleteOrder(int id) {
        orderDao.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        orderDao.deleteById(id);
        log.info("Order with Id: {} deleted", id);
    }
}