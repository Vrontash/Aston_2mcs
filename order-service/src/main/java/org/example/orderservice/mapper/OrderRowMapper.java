package org.example.orderservice.mapper;

import org.example.orderservice.model.Order;
import org.example.orderservice.model.OrderStatus;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class OrderRowMapper  {

    public Order mapRow(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setCustomerId(rs.getInt("customer_id"));
        order.setPrice(rs.getBigDecimal("price"));
        order.setQuantity(rs.getInt("quantity"));
        order.setStatus(OrderStatus.valueOf(rs.getString("status")));
        return order;
    }
}
