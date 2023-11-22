package com.searchengine.dao;

import com.searchengine.model.RegisterUserDto;
import com.searchengine.model.User;

import java.util.List;

public interface UserDao {

    List<User> getUsers();

    User getUserById(int id);

    User getUserByUsername(String username);

    User createUser(RegisterUserDto user);
}
