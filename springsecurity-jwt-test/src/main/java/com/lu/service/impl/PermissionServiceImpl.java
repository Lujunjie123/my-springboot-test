package com.lu.service.impl;

import com.lu.mapper.PermissionMapper;
import com.lu.model.entity.Permission;
import com.lu.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;


    @Override
    public Permission findPermissionByPermissionId(Long permissionId) {
        return permissionMapper.selectById(permissionId);
    }
}






































