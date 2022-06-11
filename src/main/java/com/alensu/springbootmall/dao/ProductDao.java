package com.alensu.springbootmall.dao;

import com.alensu.springbootmall.dto.ProductRequest;
import com.alensu.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {

    Product getProductById(Integer id);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProduct(Integer productId);

    List<Product> getProducts();
}
