package com.alensu.springbootmall.dao;

import com.alensu.springbootmall.dto.UserRegisterRequest;
import com.alensu.springbootmall.model.User;

public interface UserDao {

    Integer createUser(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);

    User getUserByEmail(String email);
}
