package com.lu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lu.mapper.UserMapper;
import com.lu.model.entity.SysUser;
import com.lu.model.entity.User;
import com.lu.service.AuthService;
import com.lu.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
//    @Qualifier("sampleAuthenticationManager")
    private AuthenticationManager authenticationManager;

    @Autowired
    @Qualifier("userServiceImpl")
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public boolean register(User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", user.getUsername());
        if(userMapper.selectOne(wrapper)!=null){
            return false;
        }
        final String password = user.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        int insert = userMapper.insert(user);
        if(insert>0){
            System.out.println("注册成功");
            return true;
        }else {
            return false;
        }
    }


    @Override
    public String login(String username, String password) {
        System.out.println("=================执行了login");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        System.out.println("=================执行了login结束");
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        System.out.println("userDetails:"+userDetails+"==============================================================");
//        SysUser sysUser = (SysUser) authenticate.getPrincipal();

        return jwtTokenUtil.generateToke(userDetails);
    }

    //测试逻辑删除
    @Override
    public boolean delete(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
        int delete = userMapper.delete(wrapper);
        if(delete>0){
            System.out.println("删除成功====================");
            return true;
        }else {
            return false;
        }
    }
}

































