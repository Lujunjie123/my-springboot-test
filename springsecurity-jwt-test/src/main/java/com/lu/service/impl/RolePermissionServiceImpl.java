package com.lu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lu.mapper.RolePermissionMapper;
import com.lu.model.entity.RolePermission;
import com.lu.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public RolePermission findRolePermissionByRoleId(Long roleId) {
        QueryWrapper<RolePermission> wrapper = new QueryWrapper<RolePermission>().eq("role_id", roleId);
        return rolePermissionMapper.selectOne(wrapper);
    }
}






























