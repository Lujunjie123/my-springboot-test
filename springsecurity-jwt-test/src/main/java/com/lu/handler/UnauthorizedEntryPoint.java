package com.lu.handler;

import com.lu.constant.Constant;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//未授权统一处理
//这里面主要配置如果没有凭证，可以进行一些操作
@Component
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint{

//    private String headerValue;
//
//    public UnauthorizedEntryPoint(String headerValue){
//        this.headerValue = headerValue;
//    }


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        System.out.println("未授权开始====================================================");
//        response.setHeader(Constant.HEAD_STRING,this.headerValue);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,e.getMessage());
        System.out.println("未授权结束====================================================");
    }
}


















































