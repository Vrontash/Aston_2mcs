package org.example.orderservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.orderservice.dto.OrderDto;
import org.example.orderservice.dto.OrderIdDto;
import org.example.orderservice.model.OrderStatus;
import org.example.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> saveOrder(@Valid @RequestBody OrderDto orderDto) {
        orderDto = orderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDto);
    }

    @PostMapping("/order")
    public ResponseEntity<OrderDto> findOrder(@Valid @RequestBody OrderIdDto orderIdDto) {
        OrderDto orderDto = orderService.findOrder(orderIdDto.getOrderId());
        return ResponseEntity.status(HttpStatus.OK).body(orderDto);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDto>> findOrdersByStatus(@PathVariable OrderStatus status) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.findOrdersByStatus(status));
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> findAllOrders() {
        List<OrderDto> ordersDto = orderService.findAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(ordersDto);
    }

    @PutMapping
    public ResponseEntity<OrderDto> updateOrder(@Valid @RequestBody OrderDto orderDto) {
        orderDto = orderService.updateOrder(orderDto);
        return ResponseEntity.status(HttpStatus.OK).body(orderDto);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteOrder(@Valid @RequestBody OrderIdDto orderIdDto) {
        orderService.deleteOrder(orderIdDto.getOrderId());
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
