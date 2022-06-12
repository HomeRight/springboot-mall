package com.alensu.springbootmall.dao.impl;

import com.alensu.springbootmall.constant.ProductCategory;
import com.alensu.springbootmall.dao.ProductDao;
import com.alensu.springbootmall.dto.ProductQueryParams;
import com.alensu.springbootmall.dto.ProductRequest;
import com.alensu.springbootmall.model.Product;
import com.alensu.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao {


    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description" +
                ", created_date, last_modified_date " +
                "FROM product WHERE 1 = 1";
        Map<String, Object> map = new HashMap<>();

        //查詢條件
        if (productQueryParams.getCategory() != null) {
            sql += " AND category = :category ";
            map.put("category", productQueryParams.getCategory().toString());
        }

        if (productQueryParams.getSearch() != null) {
            sql += " AND product_name LIKE :product_name ";
            map.put("product_name", "%" + productQueryParams.getSearch() + "%");
        }

        //排序
        sql += " ORDER BY " + productQueryParams.getOrderBy() + " " + productQueryParams.getSort();


        //分頁
        sql += " LIMIT :limit OFFSET :offset ";
        map.put("limit", productQueryParams.getLimit());
        map.put("offset", productQueryParams.getOffset());

        return jdbcTemplate.query(sql, map, new ProductRowMapper());
    }

    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {
        String sql = "SELECT COUNT(*) FROM product WHERE 1 = 1";
        Map<String, Object> map = new HashMap<>();

        //查詢條件
        if (productQueryParams.getCategory() != null) {
            sql += " AND category = :category ";
            map.put("category", productQueryParams.getCategory().toString());
        }

        if (productQueryParams.getSearch() != null) {
            sql += " AND product_name LIKE :product_name ";
            map.put("product_name", "%" + productQueryParams.getSearch() + "%");
        }


        //queryForObject通常用來取得count
        return jdbcTemplate.queryForObject(sql, map, Integer.class);
    }

    @Override
    public Product getProductById(Integer productId) {


        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description" +
                ", created_date, last_modified_date " +
                "FROM product WHERE product_id = :productId ";
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        List<Product> productList = jdbcTemplate.query(sql, map, new ProductRowMapper());

        return productList.size() > 0 ? productList.get(0) : null;
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {

        String sql = "INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)" +
                "VALUES ( :product_name, :category, :image_url , :price , :stock , :description ," +
                "   :created_date, :last_modified_date )";

        Map<String, Object> map = new HashMap<>();
        map.put("product_name", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("image_url", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        Date now = new Date();

        map.put("created_date", now);
        map.put("last_modified_date", now);


        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : null;
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql = "UPDATE product SET product_name = :product_name, category = :category, image_url = :image_url," +
                " price = :price, stock = :stock, description = :description, last_modified_date = :last_modified_date" +
                " WHERE product_id = :product_id";

        Map<String, Object> map = new HashMap<>();
        map.put("product_name", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("image_url", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());
        map.put("product_id", productId);

        Date now = new Date();
        map.put("last_modified_date", now);
        jdbcTemplate.update(sql, map);
    }

    @Override
    public void deleteProduct(Integer productId) {
        String sql = "DELETE FROM product WHERE product_id = :product_id";
        Map<String, Object> map = new HashMap<>();

        map.put("product_id", productId);

        jdbcTemplate.update(sql, map);
    }


}
