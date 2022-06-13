package com.alensu.springbootmall.dao;

import com.alensu.springbootmall.dto.UserRegisterRequest;
import com.alensu.springbootmall.model.User;

public interface UserDao {

    Integer register(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);
}
