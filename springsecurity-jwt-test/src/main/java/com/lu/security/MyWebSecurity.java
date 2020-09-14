package com.lu.security;

import com.lu.filter.JwtTokenFilter;
import com.lu.handler.UnauthorizedEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MyWebSecurity extends WebSecurityConfigurerAdapter {

    //加密配置
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 自定义用户认证逻辑    implements UserDetailsService
     */
    @Autowired
    @Qualifier("userServiceImpl")
    private UserDetailsService userService;

    /**
     * token认证过滤器   extends OncePerRequestFilter        最先执行
     */
    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    /**
     * 认证失败处理类  implements AuthenticationEntryPoint
     */
    @Autowired
    private UnauthorizedEntryPoint unauthorizedEntryPoint;

//    @Autowired
//    AuthenticationProvider authenticationProvider;


//    解决 无法直接注入 AuthenticationManager
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    //认证配置 // 该方法是登录的时候会进入
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //使用userService方式认证 存在的问题:所有的链接都需要进行身份验证
        auth.userDetailsService(userService).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                return passwordEncoder.encode(rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                return passwordEncoder.matches(rawPassword, encodedPassword);
            }
        });
//                .and().authenticationProvider(authenticationProvider);

//        auth.authenticationProvider(new JwtAuthenticationProvider(userService))
    }

    //授权配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // CSRF禁用，因为不使用session
                .csrf().disable()
                // 认证失败处理类
//                .authenticationEntryPoint(new UnauthorizedEntryPoint("Basic realm=\"MyApp\""))
                .exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint)
                // 基于token，所以不需要session
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 过滤请求
                .and().authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.POST, "/authentication/**").permitAll()
                .antMatchers(HttpMethod.POST).authenticated()
                .antMatchers(HttpMethod.PUT).authenticated()
                .antMatchers(HttpMethod.DELETE).authenticated()
                .antMatchers(HttpMethod.GET).authenticated()
                .and()
                .headers().frameOptions().disable().and()
                // 添加JWT filter         //在用户信息认证前先检测token合法性
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

//                .anyRequest().authenticated()
//                .and().exceptionHandling()
//                .authenticationEntryPoint(new UnauthorizedEntryPoint("Basic realm=\"MyApp\""))
//                .and()
//                .addFilter(new TokenLoginFilter())
//                .addFilter(new TokenAuthenticationFilter(authenticationManager()))
//                .formLogin().permitAll()
//                .and().logout().permitAll();
    }
}
























