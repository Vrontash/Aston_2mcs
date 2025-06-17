package org.example.warehouseservice.exception;

public class ItemNotFoundException extends RuntimeException{
    public ItemNotFoundException(int id) {
        super("Item " + id + " not found");
    }
}
