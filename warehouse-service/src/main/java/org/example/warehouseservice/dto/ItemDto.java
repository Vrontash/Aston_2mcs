package org.example.warehouseservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    @NotNull
    private Integer itemId;

    private String itemName;

    @NotNull
    @Min(value = 0, message = "Quantity must be at least 0")
    private int quantity;

}
