package com.alensu.springbootmall.dao.impl;

import com.alensu.springbootmall.dao.OrderDao;
import com.alensu.springbootmall.dto.CreateOrderRequest;
import com.alensu.springbootmall.dto.OrderQueryParams;
import com.alensu.springbootmall.dto.ProductQueryParams;
import com.alensu.springbootmall.model.Order;
import com.alensu.springbootmall.model.OrderItem;
import com.alensu.springbootmall.rowmapper.OrderItemMapper;
import com.alensu.springbootmall.rowmapper.OrderMapper;
import com.alensu.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.DataTruncation;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDaoImpl implements OrderDao {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;


    @Override
    public Integer createOrder(Integer userId, Integer totalAmount) {

        String sql = "insert into order_summary(user_id, total_amount, created_date, last_modified_date) values (" +
                ":userId, :totalAmount, :createdDate, :lastModifiedDate" +
                ")";

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("totalAmount", totalAmount);
        Date date = new Date();
        map.put("createdDate", date);
        map.put("lastModifiedDate", date);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : null;
    }

    @Override
    public void createOrderItems(Integer orderId, List<OrderItem> orderItemList) {


        //使用for loop 一條一條inset太沒效率
//        for (OrderItem orderItem : orderItemList) {
//
//            String sql = "insert into order_item(order_id, product_id, quantity, amount) values (" +
//                    ":orderId, :productId, :quantity, :amount" +
//                    ")";
//
//            Map<String, Object> map = new HashMap<>();
//            map.put("orderId", orderId);
//            map.put("productId", orderItem.getProductId());
//            map.put("quantity", orderItem.getQuantity());
//            map.put("amount", orderItem.getAmount());
//
//            jdbcTemplate.update(sql, map);
//        }

        //使用batch update
        String sql = "insert into order_item(order_id, product_id, quantity, amount) values (" +
                ":orderId, :productId, :quantity, :amount" +
                ")";


        MapSqlParameterSource[] mapSqlParameterSources = new MapSqlParameterSource[orderItemList.size()];

        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);

            mapSqlParameterSources[i] = new MapSqlParameterSource();
            mapSqlParameterSources[i].addValue("orderId", orderId);
            mapSqlParameterSources[i].addValue("productId", orderItem.getProductId());
            mapSqlParameterSources[i].addValue("quantity", orderItem.getQuantity());
            mapSqlParameterSources[i].addValue("amount", orderItem.getAmount());
        }

        jdbcTemplate.batchUpdate(sql, mapSqlParameterSources);


    }

    @Override
    public Order getOrderById(Integer orderId) {

        String sql = "select order_id, total_amount, user_id, created_date, last_modified_date from order_summary where order_id = :orderId";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        List<Order> orderList = jdbcTemplate.query(sql, map, new OrderMapper());

        return orderList.size() > 0 ? orderList.get(0) : null;
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        String sql = "select oi.order_item_id, oi.order_id, oi.product_id, oi.quantity, oi.amount, " +
                "p.product_name, p.image_url from order_item oi " +
                "left join product p on oi.product_id = p.product_id " +
                "where oi.order_id = :orderId";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        List<OrderItem> orderItemList = jdbcTemplate.query(sql, map, new OrderItemMapper());

        return orderItemList.size() > 0 ? orderItemList : null;

    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        String sql = "SELECT order_id, user_id, total_amount, created_date, last_modified_date " +
                "FROM order_summary WHERE 1 = 1 AND user_id = :userId";
        Map<String, Object> map = new HashMap<>();

        //查詢條件
        sql = addFilterSql(sql, map, orderQueryParams);

        //排序
        sql += " ORDER BY created_date DESC ";

        //分頁
        sql += " LIMIT :limit OFFSET :offset ";
        map.put("limit", orderQueryParams.getLimit());
        map.put("offset", orderQueryParams.getOffset());

        return jdbcTemplate.query(sql, map, new OrderMapper());
    }

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        String sql = "SELECT COUNT(*) FROM order_summary WHERE 1 = 1";
        Map<String, Object> map = new HashMap<>();
        map.put("userId", orderQueryParams.getUserId());

        sql = addFilterSql(sql, map, orderQueryParams);

        //queryForObject通常用來取得count
        return jdbcTemplate.queryForObject(sql, map, Integer.class);
    }


    private String addFilterSql(String sql, Map<String, Object> map, OrderQueryParams orderQueryParams) {

        if (orderQueryParams.getUserId() != null) {
            sql += " AND user_id = :userId ";
            map.put("userId", orderQueryParams.getUserId());
        }

        return sql;
    }
}
