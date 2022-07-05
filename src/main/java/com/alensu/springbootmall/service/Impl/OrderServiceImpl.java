package com.alensu.springbootmall.service.Impl;

import com.alensu.springbootmall.dao.OrderDao;
import com.alensu.springbootmall.dao.ProductDao;
import com.alensu.springbootmall.dao.UserDao;
import com.alensu.springbootmall.dto.BuyItem;
import com.alensu.springbootmall.dto.CreateOrderRequest;
import com.alensu.springbootmall.model.Order;
import com.alensu.springbootmall.model.OrderItem;
import com.alensu.springbootmall.model.Product;
import com.alensu.springbootmall.model.User;
import com.alensu.springbootmall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);


    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {

        User user = userDao.getUserById(userId);

        if (user == null) {
            log.warn("userId:{} 不存在" + userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductById(buyItem.getProductId());

            //檢查商品狀態
            if (product == null) {
                log.warn("無此商品:{}", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } else if (product.getStock() < buyItem.getQuantity()) {
                log.warn("商品:{} 庫存不足,剩餘庫存:{} 欲買數量:{}", buyItem.getProductId(), product.getStock(), buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            //扣除商品庫存
            productDao.updateStock(product.getProductId(), product.getStock() - buyItem.getQuantity());

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
