package org.example.warehouseservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.example.warehouseservice.dto.ItemDto;
import org.example.warehouseservice.dto.OrderDto;
import org.example.warehouseservice.exception.ItemNotFoundException;
import org.example.warehouseservice.model.Item;
import org.example.warehouseservice.model.OrderStatus;
import org.example.warehouseservice.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final OrderService orderService;

    public ItemDto createItem(ItemDto itemDto) {
        Item item = warehouseRepository.save(dtoToEntity(itemDto));
        log.info("Item created : {}", item);
        return entityToDto(item);
    }

    public ItemDto findItem(int id) {
        log.info("Get item with Id: {}", id);
        return warehouseRepository.findById(id)
                .map(this::entityToDto)
                .orElseThrow(() -> new ItemNotFoundException(id));
    }

    public List<ItemDto> findAllItems() {
        log.info("Retrieved all items");
        return warehouseRepository.findAll()
                .stream()
                .map(this::entityToDto)
                .toList();
    }
    public Map<Integer, Integer> findAllItemsQuantity(){
        return findAllItems().stream()
                .collect(Collectors.toMap(
                        ItemDto::getItemId,
                        ItemDto:: getQuantity
                ));
    }
    public ItemDto updateItem(ItemDto itemDto) {
        Item item = warehouseRepository.findById(itemDto.getItemId())
                .orElseThrow(() -> new ItemNotFoundException(itemDto.getItemId()));
        Optional.ofNullable(itemDto.getItemName())
                .filter(name -> !name.isBlank())
                .ifPresent(item::setItemName);
        item.setQuantity(itemDto.getQuantity());
        log.info("Item updated : {}", item);
        return entityToDto(warehouseRepository.save(item));
    }

    public void deleteItem(int id) {
        warehouseRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
        warehouseRepository.deleteById(id);
        log.info("Item deleted with Id: {}", id);
    }

    @Transactional
    public void checkSingleOrderForStatusChange(OrderDto orderDto, Map<Integer, Integer> itemsQuantity) {
        Integer availableQuantity = itemsQuantity.get(orderDto.getItemId());

        if(availableQuantity != null && availableQuantity >= orderDto.getQuantity()) {
            orderDto.setStatus(OrderStatus.APPROVED);
            itemsQuantity.put(orderDto.getItemId(), availableQuantity - orderDto.getQuantity());
            updateItem(new ItemDto(orderDto.getItemId(), null, availableQuantity - orderDto.getQuantity()));
        }
        else {
            orderDto.setStatus(OrderStatus.CANCELED);
        }

        log.info("OrderDto's status with Id: {}  was changed to {}", orderDto.getOrderId(), orderDto.getStatus());
        orderService.updateOrder(orderDto);
    }

    @Transactional
    public List<OrderDto> checkOrdersForStatusChange() {
        List<OrderDto> orders = orderService.findOrdersByStatusCreated(OrderStatus.CREATED);
        Map<Integer, Integer> items = findAllItemsQuantity();
        for (OrderDto orderDto : orders) {
            checkSingleOrderForStatusChange(orderDto, items);
        }
        return orders;
    }

    private Item dtoToEntity(ItemDto itemDto) {
        Item item = new Item();
        item.setItemName(itemDto.getItemName());
        item.setQuantity(itemDto.getQuantity());
        return item;
    }

    private ItemDto entityToDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setItemId(item.getItemId());
        itemDto.setItemName(item.getItemName());
        itemDto.setQuantity(item.getQuantity());
        return itemDto;
    }
}