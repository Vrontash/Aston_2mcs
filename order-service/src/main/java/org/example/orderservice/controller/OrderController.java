package org.example.orderservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.orderservice.dto.OrderDto;
import org.example.orderservice.model.OrderStatus;
import org.example.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDto>> findOrdersByStatus(@PathVariable OrderStatus status) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(orderService.findOrdersByStatus(status));
    }
    @PostMapping
    public ResponseEntity<OrderDto> saveOrder(@Valid @RequestBody OrderDto orderDto) {
        orderDto = orderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> findOrder(@PathVariable int id) {
        OrderDto orderDto = orderService.findOrder(id);
        return ResponseEntity.status(HttpStatus.OK).body(orderDto);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable int id) {
        orderService.deleteOrder(id);
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
