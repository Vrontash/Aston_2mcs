package org.example.warehouseservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.warehouseservice.dto.ItemDto;

import org.example.warehouseservice.dto.OrderDto;
import org.example.warehouseservice.service.WarehouseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;

    @PutMapping("/orders")
    public ResponseEntity<?> processCreatedOrders() {
        List<OrderDto> orders = warehouseService.checkOrdersForStatusChange();
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }
    @PostMapping
    public ResponseEntity<?> saveItem(@Valid @RequestBody ItemDto itemDto) {
        itemDto = warehouseService.createItem(itemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findItem(@PathVariable int id) {
        ItemDto itemDto = warehouseService.findItem(id);
        return ResponseEntity.status(HttpStatus.OK).body(itemDto);
    }

    @GetMapping
    public ResponseEntity<?> findAllItem() {
        List<ItemDto> itemDtoList = warehouseService.findAllItems();
        return ResponseEntity.status(HttpStatus.OK).body(itemDtoList);
    }

    @PutMapping
    public ResponseEntity<?> updateItem(@Valid @RequestBody ItemDto itemDto) {
        itemDto = warehouseService.updateItem(itemDto);
        return ResponseEntity.status(HttpStatus.OK).body(itemDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable int id) {
        warehouseService.deleteItem(id);
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
