//package com.lu.filter;
//
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///*
// * 访问过滤器
// * 自定义JWT认证过滤器
// * 该类继承自BasicAuthenticationFilter，在doFilterInternal方法中，
// * 从http头的Authorization 项读取token数据，然后用Jwts包提供的方法校验token的合法性。
// * 如果校验通过，就认为这是一个取得授权的合法请求
// */
//
//public class TokenAuthenticationFilter extends BasicAuthenticationFilter {
//
//
//    public TokenAuthenticationFilter(AuthenticationManager authenticationManager) {
//        super(authenticationManager);
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        logger.info("================="+request.getRequestURI());
//        super.doFilterInternal(request, response, chain);
//    }
//
//
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
