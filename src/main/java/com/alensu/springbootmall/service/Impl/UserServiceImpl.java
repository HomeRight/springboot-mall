package com.alensu.springbootmall.service.Impl;

import com.alensu.springbootmall.dao.UserDao;
import com.alensu.springbootmall.dto.UserRegisterRequest;
import com.alensu.springbootmall.model.User;
import com.alensu.springbootmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        return userDao.register(userRegisterRequest);
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }
}
