package com.alensu.springbootmall.dao;

import com.alensu.springbootmall.model.Product;

public interface ProductDao {

    Product getProductById(Integer id);
}
