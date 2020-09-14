package com.lu.filter;

import com.lu.constant.Constant;
import com.lu.model.entity.SysUser;
import com.lu.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//验证token合法性    最先进入
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserDetailsService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    //判断token合法性
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        System.out.println("判断token合法性开始================================");
        //从消息头中获取token,根据token获取username,根据username查询数据库是否存在,将主体信息设置到Security上下文中
        //生成的token默认key为Authorization   value是Bearer+token
        String header = request.getHeader(Constant.HEAD_STRING);
        if(header != null && header.startsWith(Constant.TOKEN_PREFIX)){
            String token = header.substring(Constant.TOKEN_PREFIX.length()+1); //默认有个空格


            String username = jwtTokenUtil.getUsernameFromToken(token);
            System.out.println(username+"&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");

            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userService.loadUserByUsername(username);

                if(jwtTokenUtil.validateToken(token,userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
            }
        }
        System.out.println("判断token合法性结束================================");
        chain.doFilter(request,response);
    }
}



































