package org.example.warehouseservice.service;

import jakarta.validation.Valid;
import org.example.warehouseservice.dto.OrderDto;
import org.example.warehouseservice.model.OrderStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "order-service")
public interface OrderService {

    @GetMapping("/orders/status/{status}")
    List<OrderDto> findOrdersByStatusCreated(@PathVariable OrderStatus status);

    @PutMapping("/orders")
    OrderDto updateOrder(@Valid @RequestBody OrderDto orderDto);
}
