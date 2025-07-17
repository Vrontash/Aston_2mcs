package org.example.orderservice.model;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @NotNull
    private int orderId;

    @NotNull
    private int customerId;

    @NotNull
    private int itemId;

    @NotNull
    private int quantity;

    @NotNull
    private BigDecimal price;

    @NotNull
    private OrderStatus status;

}
