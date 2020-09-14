//package com.lu.filter;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.lu.constant.Constant;
//import com.lu.model.entity.User;
//import com.lu.util.JwtTokenUtil;
//import com.sun.javafx.collections.MappingChange;
//import io.jsonwebtoken.Claims;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.*;
//
//// 登录过滤器，继承UsernamePasswordAuthenticationFilter，对用户名密码进行登录校验
//public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private  JwtTokenUtil jwtTokenUtil;
//
//    // 尝试身份认证(接收并解析用户凭证)
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        System.out.println("尝试身份认证====================================================");
//        try {
//            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
//            return authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword(),new ArrayList<>()));
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    //登陆成功  将token放入响应头中
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
//        System.out.println("登陆成功====================================================");
//        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
//        List roleList = new ArrayList<>();
//        for (GrantedAuthority authority : authorities) {
//            roleList.add(authority.getAuthority());
//        }
//        String toke = jwtTokenUtil.generateToke(authResult.getName() + "-" + roleList);
//        response.setHeader(Constant.HEAD_STRING,Constant.TOKEN_PREFFIX+toke);
//    }
//
//    //登陆失败
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
//        System.out.println("登陆失败====================================================");
//        try {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,failed.getMessage());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
