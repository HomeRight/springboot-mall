package com.alensu.springbootmall.dao.impl;

import com.alensu.springbootmall.dao.UserDao;
import com.alensu.springbootmall.dto.UserRegisterRequest;
import com.alensu.springbootmall.model.User;
import com.alensu.springbootmall.rowmapper.UserRowMapper;
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
public class UserDaoImpl implements UserDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;


    @Override
    public Integer createUser(UserRegisterRequest userRegisterRequest) {

        String sql = "INSERT INTO USER(email ,password ,created_date ,last_modified_date) VALUES( :email , :password , :created_date , :last_modified_date )";

        Map<String, Object> map = new HashMap<>();
        map.put("email", userRegisterRequest.getEmail());
        map.put("password", userRegisterRequest.getPassword());

        Date now = new Date();
        map.put("created_date", now);
        map.put("last_modified_date", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : null;
    }

    @Override
    public User getUserById(Integer userId) {

        String sql = "SELECT user_id ,email ,password ,created_date ,last_modified_date FROM USER WHERE user_id = :userId ";
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        List<User> userList = jdbcTemplate.query(sql, map, new UserRowMapper());

        return userList.size() > 0 ? userList.get(0) : null;
    }

    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT user_id ,email ,password ,created_date ,last_modified_date FROM USER WHERE email = :email ";
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);

        List<User> userList = jdbcTemplate.query(sql, map, new UserRowMapper());

        return userList.size() > 0 ? userList.get(0) : null;
    }
}
