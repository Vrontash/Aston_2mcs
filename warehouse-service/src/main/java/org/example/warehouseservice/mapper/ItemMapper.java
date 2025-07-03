package org.example.warehouseservice.mapper;

import org.example.warehouseservice.dto.ItemDto;
import org.example.warehouseservice.model.Item;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemDto toItemDto(Item item);
    Item toItem(ItemDto itemDto);

    @Mapping(target = "itemId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateItemFromDto(ItemDto itemDto, @MappingTarget Item item);

}
