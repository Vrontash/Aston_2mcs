package org.example.orderservice.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.orderservice.model.OrderStatus;

import java.math.BigDecimal;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    @NotNull
    private Integer orderId;

    @NotNull
    private Integer customerId;

    @NotNull
    private Integer itemId;

    @NotNull
    @Min(value = 0, message = "Quantity must be at least 0")
    private Integer quantity;

    @NotNull
    @Min(value = 0, message = "Price must be at least 0")
    private BigDecimal price;

    @NotNull
    private OrderStatus status;
}
