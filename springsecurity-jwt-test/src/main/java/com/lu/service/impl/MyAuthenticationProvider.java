//package com.lu.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.lu.mapper.UserMapper;
//import com.lu.model.entity.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//@Component
//public class MyAuthenticationProvider implements AuthenticationProvider {
//
//    @Autowired
//    UserMapper userMapper;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        System.out.println("=======================执行MyAuthenticationProvider");
//        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", authentication.getName());
//        User user = userMapper.selectOne(wrapper);
//        if (user == null) {
//            throw new UsernameNotFoundException("用户不存在");
//        }
//        String password = (String) authentication.getCredentials();
//        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//        return new UsernamePasswordAuthenticationToken(user, password, authorities);
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return authentication.equals(UsernamePasswordAuthenticationToken.class);
//    }
//}
