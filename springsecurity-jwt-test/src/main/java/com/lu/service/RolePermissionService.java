package com.lu.service;


import com.lu.model.entity.RolePermission;

public interface RolePermissionService {

    RolePermission findRolePermissionByRoleId(Long roleId);
}
