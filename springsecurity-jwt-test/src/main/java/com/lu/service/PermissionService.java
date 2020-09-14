package com.lu.service;

import com.lu.model.entity.Permission;

public interface PermissionService {

    Permission findPermissionByPermissionId(Long permissionId);
}
