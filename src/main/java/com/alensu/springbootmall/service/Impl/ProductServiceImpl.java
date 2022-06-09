package com.alensu.springbootmall.service.Impl;

import com.alensu.springbootmall.dao.ProductDao;
import com.alensu.springbootmall.model.Product;
import com.alensu.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public Product getProductById(Integer id) {

        return productDao.getProductById(id);
    }
}
