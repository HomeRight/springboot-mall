package com.alensu.springbootmall.service;

import com.alensu.springbootmall.constant.ProductCategory;
import com.alensu.springbootmall.dto.ProductQueryParams;
import com.alensu.springbootmall.dto.ProductRequest;
import com.alensu.springbootmall.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface ProductService {

    Product getProductById(Integer id);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProduct(Integer productId);

    List<Product> getProducts(ProductQueryParams productQueryParams);

    Integer countProduct(ProductQueryParams productQueryParams);
}
