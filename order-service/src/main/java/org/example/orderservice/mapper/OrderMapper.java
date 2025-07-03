package org.example.orderservice.mapper;

import org.example.orderservice.dto.OrderDto;
import org.example.orderservice.model.Order;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto toOrderDto(Order order);

    Order toOrder(OrderDto orderDto);

    @Mapping(target = "orderId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateOrderFromDto(OrderDto orderDto, @MappingTarget Order order);
}
