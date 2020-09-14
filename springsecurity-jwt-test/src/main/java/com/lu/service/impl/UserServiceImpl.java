package com.lu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lu.mapper.UserMapper;
import com.lu.model.entity.Role;
import com.lu.model.entity.SysUser;
import com.lu.model.entity.User;
import com.lu.model.entity.UserRole;
import com.lu.service.RoleService;
import com.lu.service.UserRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

//    @Autowired
//    private RolePermissionService rolePermissionService;
//
//    @Autowired
//    private PermissionService permissionService;

//    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            System.out.println("=======================执行loadUserByUsername");
            QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
            User user = userMapper.selectOne(wrapper);
            if (user == null) {
                throw new UsernameNotFoundException("用户不存在");
            }
            UserRole userRole = userRoleService.findUserRoleByUserId(user.getId());
            System.out.println("userRole==================================================:"+userRole);

            Role role = roleService.findRoleByRoleId(userRole.getRoleId());
            System.out.println("role===================================================:"+role.getName());

            List<GrantedAuthority> authorities  = new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority(role.getName()));

            SysUser sysUser = new SysUser(user.getUsername(),user.getPassword(),authorities);
//            BeanUtils.copyProperties(user,sysUser);
            return sysUser;

//
////        RolePermission rolePermission = rolePermissionService.findRolePermissionByRoleId(role.getId());
////
////        Permission permission = permissionService.findPermissionByPermissionId(rolePermission.getPermissionId());
//
////                AuthorityUtils.commaSeparatedStringToAuthorityList(permission.getName()+","+role.getName()));
//            //设置权限和角色
//            // 1. commaSeparatedStringToAuthorityList放入角色时需要加前缀ROLE_，而在controller使用时不需要加ROLE_前缀
//            // 2. 放入的是权限时，不能加ROLE_前缀，hasAuthority与放入的权限名称对应即可
//            System.out.println("=======================结束loadUserByUsername");


//

//        return createUser(user);
    }

//    private UserDetails createUser(User user) {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_DEVELOP"));
//
//        return new SysUser(user.getUsername(),user.getPassword(),
//                authorities);
//    }

//    private List<GrantedAuthority> getAuthorities(){
//        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
//        authList.add(new SimpleGrantedAuthority("ROLE_USER"));
//        authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        return authList;
//    }
}






























