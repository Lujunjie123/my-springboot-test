package com.lu.service;

import com.lu.model.entity.User;


public interface AuthService {

    boolean register(User user);

    String login(String username,String password);

    boolean delete(String username);
}
