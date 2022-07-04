package com.alensu.springbootmall.rowmapper;

import com.alensu.springbootmall.model.OrderItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemMapper implements RowMapper<OrderItem> {

    @Override
    public OrderItem mapRow(ResultSet resultSet, int i) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(resultSet.getInt("product_id"));
        orderItem.setOrderId(resultSet.getInt("order_id"));
        orderItem.setOrderItemId(resultSet.getInt("order_item_id"));
        orderItem.setQuantity(resultSet.getInt("quantity"));
        orderItem.setAmount(resultSet.getInt("amount"));

        orderItem.setImageUrl(resultSet.getString("image_url"));
        orderItem.setProductName(resultSet.getString("product_name"));

        return orderItem;
    }
}
