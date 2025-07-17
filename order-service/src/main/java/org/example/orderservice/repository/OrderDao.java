package org.example.orderservice.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderservice.mapper.OrderRowMapper;
import org.example.orderservice.model.Order;


import org.example.orderservice.model.OrderStatus;
import org.springframework.stereotype.Repository;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
@Slf4j
public class OrderDao {

    private final DataSource dataSource;
    private final OrderRowMapper orderRowMapper;

    public Optional<Order> findById(int id) {
        String sql = "SELECT * FROM orders WHERE order_id = ?";
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return Optional.ofNullable(orderRowMapper.mapRow(resultSet));
        }catch (SQLException e){
            throw new RuntimeException("Error finding order by id", e);
        }
        return Optional.empty();
    }

    public List<Order> findAll() {
        String sql = "SELECT * FROM orders";
        List<Order> orders = new ArrayList<>();
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                orders.add(orderRowMapper.mapRow(resultSet));
            }

        }catch (SQLException e){
            throw new RuntimeException("Error finding orders", e);
        }
        return orders;
    }

    public List<Order> findByStatus(OrderStatus status) {
        String sql = "SELECT * FROM orders WHERE status = ?";
        List<Order> orders = new ArrayList<>();
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, status, Types.OTHER);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                orders.add(orderRowMapper.mapRow(resultSet));
            }
        } catch (SQLException e){
            throw new RuntimeException("Error finding order by status", e);
        }

        return orders;
    }

    public Order save(Order order) {
        String sql = "INSERT INTO orders (customer_id, item_id, quantity, price, status) VALUES (?, ?, ?, ?, ?)";
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, order.getCustomerId());
            preparedStatement.setInt(2, order.getItemId());
            preparedStatement.setInt(3, order.getQuantity());
            preparedStatement.setBigDecimal(4, order.getPrice());
            preparedStatement.setObject(5, order.getStatus().name(),  Types.OTHER);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                order.setOrderId(generatedKeys.getInt(1));
            }
        }catch (SQLException e){
            throw new RuntimeException("Error saving order", e);
        }
        return order;
    }

    public Order update(Order order) {
        String sql = "UPDATE orders SET item_id = ?, customer_id = ?,  quantity = ?, price = ?, status = ? WHERE order_id = ?";
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, order.getItemId());
            preparedStatement.setInt(2, order.getCustomerId());
            preparedStatement.setInt(3, order.getQuantity());
            preparedStatement.setBigDecimal(4, order.getPrice());
            preparedStatement.setObject(5, order.getStatus().name(),  Types.OTHER);
            preparedStatement.setInt(6, order.getOrderId());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("Error updating order", e);
        }
        return order;
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("Error deleting order", e);
        }
    }
}
