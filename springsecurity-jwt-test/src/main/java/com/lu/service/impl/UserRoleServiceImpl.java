package com.lu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lu.mapper.UserRoleMapper;
import com.lu.model.entity.UserRole;
import com.lu.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;


    @Override
    public UserRole findUserRoleByUserId(Long userId) {
        QueryWrapper<UserRole> wrapper = new QueryWrapper<UserRole>().eq("user_id", userId);
        return userRoleMapper.selectOne(wrapper);
    }
}




























