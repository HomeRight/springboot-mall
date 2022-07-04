package com.alensu.springbootmall.service;

import com.alensu.springbootmall.dto.CreateOrderRequest;
import com.alensu.springbootmall.model.Order;

public interface OrderService {

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

    Order getOrderById(Integer orderId);
}
