package com.alensu.springbootmall.service;

import com.alensu.springbootmall.dto.UserLoginRequest;
import com.alensu.springbootmall.dto.UserRegisterRequest;
import com.alensu.springbootmall.model.User;
import org.springframework.data.relational.core.sql.In;

public interface UserService {

    Integer register(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);

    User login(UserLoginRequest userLoginRequest);
}
