package org.example.orderservice.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
