package com.lu.service;

import com.lu.model.entity.UserRole;

import java.util.List;

public interface UserRoleService {

    UserRole findUserRoleByUserId(Long userId);
}
