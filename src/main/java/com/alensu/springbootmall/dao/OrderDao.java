package com.alensu.springbootmall.dao;

import com.alensu.springbootmall.dto.CreateOrderRequest;
import com.alensu.springbootmall.model.Order;
import com.alensu.springbootmall.model.OrderItem;
import org.springframework.data.relational.core.sql.In;

import java.util.List;

public interface OrderDao {

    Integer createOrder(Integer userId, Integer createOrderRequest);

    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);

    Order getOrderById(Integer orderId);

    List<OrderItem> getOrderItemsByOrderId(Integer orderId);
}
