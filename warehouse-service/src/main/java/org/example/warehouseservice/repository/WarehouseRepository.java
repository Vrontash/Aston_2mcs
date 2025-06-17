package org.example.warehouseservice.repository;

import org.example.warehouseservice.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Item, Integer> {
}
