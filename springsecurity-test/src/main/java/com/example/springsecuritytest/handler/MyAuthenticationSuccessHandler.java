package com.example.springsecuritytest.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//认证成功处理器
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private RequestCache requestCache = new HttpSessionRequestCache();
    private DefaultRedirectStrategy defaultRedirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

    }
    /*
       {"authorities":[{"authority":"ROLE_ADMIN"}],"details":{"remoteAddress":"0:0:0:0:0:0:0:1",
       "sessionId":"81ABEEFE98B4B66E83A601D186B40B3E"},"authenticated":true,"principal":{"username":"xiaolu",
       "password":"$2a$10$DFd0E03n/ErdBW9YDTDWzu9Bs4OlrEnY8BY0UYHorwmi2A7EGHkk2","enabled":true,
       "accountNonLocked":true,"credentialsNonExpired":true,"accountNonExpired":true,
       "authorities":[{"authority":"ROLE_ADMIN"}]},"credentials":null,"name":"xiaolu"}
        */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //输出基本信息
//        response.setContentType("application/json;charset=utf-8");
//        response.getWriter().write(new ObjectMapper().writeValueAsString(authentication));

        //原来指定的url
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        defaultRedirectStrategy.sendRedirect(request,response,savedRequest.getRedirectUrl());
        //自定义url
//        defaultRedirectStrategy.sendRedirect(request,response,"/index");
    }
}








































