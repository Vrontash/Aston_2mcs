package org.example.orderservice.exception;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(int orderId) {
        super("Order with id " + orderId + " not found");
    }
}
