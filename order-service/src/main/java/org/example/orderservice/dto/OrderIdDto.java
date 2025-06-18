package org.example.orderservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderIdDto {

    @NotNull
    @Min(value = 0, message = "OrderId can not be negative")
    private int orderId;
}
