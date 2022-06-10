package com.alensu.springbootmall.dao;

import com.alensu.springbootmall.dto.ProductRequest;
import com.alensu.springbootmall.model.Product;

public interface ProductDao {

    Product getProductById(Integer id);

    Integer createProduct(ProductRequest productRequest);
}
