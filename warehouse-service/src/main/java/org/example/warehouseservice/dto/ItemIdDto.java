package org.example.warehouseservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ItemIdDto {

    @NotNull
    @Min(value = 0, message = "ItemId can not be negative")
    private Integer itemId;
}
