package com.example.springsecuritytest.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springsecuritytest.entity.User;
import com.example.springsecuritytest.entity.UserDetail;
import com.example.springsecuritytest.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
        User user = userMapper.selectOne(wrapper);
        if(user == null){
            throw new UsernameNotFoundException("用户找不到");
        }

        return new UserDetail(user.getUsername(),user.getPassword());
    }
}



































