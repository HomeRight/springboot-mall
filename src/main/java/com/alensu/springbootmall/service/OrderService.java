package com.alensu.springbootmall.service;

import com.alensu.springbootmall.dto.CreateOrderRequest;
import com.alensu.springbootmall.dto.OrderQueryParams;
import com.alensu.springbootmall.model.Order;

import java.util.List;

public interface OrderService {

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

    Order getOrderById(Integer orderId);

    List<Order> getOrders(OrderQueryParams orderQueryParams);

    Integer countOrder(OrderQueryParams orderQueryParams);
}
