package com.alensu.springbootmall.service.Impl;

import com.alensu.springbootmall.dao.OrderDao;
import com.alensu.springbootmall.dao.ProductDao;
import com.alensu.springbootmall.dto.BuyItem;
import com.alensu.springbootmall.dto.CreateOrderRequest;
import com.alensu.springbootmall.model.Order;
import com.alensu.springbootmall.model.OrderItem;
import com.alensu.springbootmall.model.Product;
import com.alensu.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;


    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {

        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductById(buyItem.getProductId());

            //計算總價錢
            int amount = product.getPrice() * buyItem.getQuantity();
            totalAmount += amount;

            //轉換buyItem to orderItemList
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);
            orderItemList.add(orderItem);
        }


        Integer orderId = orderDao.createOrder(userId, totalAmount);

        orderDao.createOrderItems(orderId, orderItemList);

        return orderId;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        Order order = orderDao.getOrderById(orderId);
        List<OrderItem> orderItems = orderDao.getOrderItemsByOrderId(orderId);
        order.setOrderItemList(orderItems);

        return order;
    }
}
