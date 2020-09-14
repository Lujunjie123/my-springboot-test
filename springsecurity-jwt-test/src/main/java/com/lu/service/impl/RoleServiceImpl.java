package com.lu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lu.mapper.RoleMapper;
import com.lu.model.entity.Role;
import com.lu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;


    @Override
    public Role findRoleByRoleId(Long roleId) {
        return roleMapper.selectById(roleId);
    }
}
