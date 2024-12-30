package com.utkarsh.UserService.service;

import com.utkarsh.UserService.entity.User;

import java.util.List;

public interface IUserService {
    User saveUser(User user);

    List<User> getAllUser();

    User getUserById(String userId);

    void deleteUser(String userId);

    User updateUser(String userId, User user);
}
