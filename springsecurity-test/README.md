## 基于UserDetailService的认证

参考my-springboot-test中的springsecurity-test

### pom依赖

```xml
//使用StringUtils
<dependency>
   <groupId>commons-lang</groupId>
   <artifactId>commons-lang</artifactId>
   <version>2.6</version>
</dependency>

//使用验证码功能
<dependency>
   <groupId>org.springframework.social</groupId>
   <artifactId>spring-social-config</artifactId>
   <version>1.1.6.RELEASE</version>
</dependency>

//使用ObjectMapper 来json序列化输出字符
<dependency>
   <groupId>com.fasterxml.jackson.core</groupId>
   <artifactId>jackson-databind</artifactId>
</dependency>

<dependency>
   <groupId>com.baomidou</groupId>
   <artifactId>mybatis-plus-boot-starter</artifactId>
   <version>3.3.1</version>
</dependency>

<dependency>
   <groupId>org.projectlombok</groupId>
   <artifactId>lombok</artifactId>
</dependency>

<dependency>
   <groupId>mysql</groupId>
   <artifactId>mysql-connector-java</artifactId>
</dependency>

<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### 主配置类

```java
@Configuration
@EnableWebSecurity
public class BrowerSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    MyAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    MyAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    @Qualifier("userDetailServiceImpl")
    private UserDetailsService userDetailsService;

    //验证码过滤器
    @Autowired
    private ValidateCodeFilter validateCodeFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                return passwordEncoder.encode(rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                return passwordEncoder.matches(rawPassword,encodedPassword);
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class) // 添加验证码校验过滤器	验证码在登陆认证过滤器前执行
                .csrf().disable()
                .formLogin().loginPage("/login.html")	//自定义登录页
                .loginProcessingUrl("/login")			//和表单的action地址一致
                .successHandler(authenticationSuccessHandler)	//成功处理器
                .failureHandler(authenticationFailureHandler)	//失败处理器
                .and().authorizeRequests()
                .antMatchers("/login.html","/code/image").permitAll()	//验证码和登录页不拦截
                .anyRequest().authenticated();			//其他请求都需要认证
    }
}
```

### login.html登录页表单

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>登录</title>
    <link rel="stylesheet" href="css/login.css" type="text/css">
</head>
<body>
<form class="login-page" action="/login" method="post">
    <div class="form">
        <h3>账户登录</h3>
        <input type="text" placeholder="用户名" name="username" required="required" />
        <input type="password" placeholder="密码" name="password" required="required" />
        <span style="display: inline">
            <input type="text" name="imageCode" placeholder="验证码" style="width: 50%;"/>
            <img src="/code/image"/>
        </span>
        <button type="submit">登录</button>
    </div>
</form>
</body>
</html>
```

### 自定义认证规则

https://mrbird.cc/Spring-Security-Authentication.html

实现UserDetailService

```java
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
```

### 自定义安全用户

实现UserDetails

springsecurity安全用户

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail implements UserDetails {

    private String username;

    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

### 自定义失败处理器

实现AuthenticationFailureHandler

```java
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(exception.getMessage()));
    }
}
```

### 自定义成功处理器

实现AuthenticationSuccessHandler

```java
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
```

### 验证码实现

https://mrbird.cc/Spring-Security-ValidateCode.html

#### 验证码对象

```java
//验证码对象
@Data
public class ImageCode {
    //图片
    private BufferedImage image;
    //验证码
    private String code;
    //过期时间
    private LocalDateTime expireTime;

    public ImageCode(BufferedImage image, String code, int expireIn) {
        this.image = image;
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
        this.image = image;
        this.code = code;
        this.expireTime = expireTime;
    }

    public boolean isExpire() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
```

#### 创建验证码

```java
@RestController
public class ValidateController {

    public final static String SESSION_KEY_IMAGE_CODE = "SESSION_KEY_IMAGE_CODE";

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    //使用sessionStrategy将生成的验证码对象存储到Session中，并通过IO流将生成的图片输出到登录页面上
    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageCode imageCode = createImageCode();
        sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY_IMAGE_CODE, imageCode);
        ImageIO.write(imageCode.getImage(), "jpeg", response.getOutputStream());
    }


    private ImageCode createImageCode() {

        int width = 100; // 验证码图片宽度
        int height = 36; // 验证码图片长度
        int length = 4; // 验证码位数
        int expireIn = 60; // 验证码有效时间 60s

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        Random random = new Random();

        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        StringBuilder sRand = new StringBuilder();
        for (int i = 0; i < length; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand.append(rand);
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i + 6, 16);
        }
        g.dispose();
        return new ImageCode(image, sRand.toString(), expireIn);
    }

    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
```

#### 登陆页增加验证码

````html
<span style="display: inline">
    <input type="text" name="imageCode" placeholder="验证码" style="width: 50%;"/>
    <img src="/code/image"/>
</span>
````

**注意：要配置生成验证码的请求不被拦截**，还要通过`addFilterBefore`方法将`ValidateCodeFilter`验证码校验过滤器添加到了`UsernamePasswordAuthenticationFilter`前面。

#### 自定义验证码过滤器

实现OncePerRequestFilter，只调用一次

```java
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (StringUtils.equalsIgnoreCase("/login", httpServletRequest.getRequestURI())
                && StringUtils.equalsIgnoreCase(httpServletRequest.getMethod(), "post")) {
            try {
                validateCode(new ServletWebRequest(httpServletRequest));
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
    //对应登录页面的验证码<input>框name属性
    private void validateCode(ServletWebRequest servletWebRequest) throws ServletRequestBindingException {
        ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(servletWebRequest, ValidateController.SESSION_KEY_IMAGE_CODE);
        String codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(), "imageCode");

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码不能为空！");
        }
        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在！");
        }
        if (codeInSession.isExpire()) {
            sessionStrategy.removeAttribute(servletWebRequest, ValidateController.SESSION_KEY_IMAGE_CODE);
            throw new ValidateCodeException("验证码已过期！");
        }
        if (!StringUtils.equalsIgnoreCase(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不正确！");
        }
        sessionStrategy.removeAttribute(servletWebRequest, ValidateController.SESSION_KEY_IMAGE_CODE);
    }
}
```

#### 自定义验证码异常

```java
public class ValidateCodeException extends AuthenticationException {

    private static final long serialVersionUID = -2866301886317422588L;

    public ValidateCodeException(String message) {
        super(message);
    }
}
```

### 记住我功能

https://mrbird.cc/Spring-Security-RememberMe.html

记住我选项是一个很常见的功能，勾选记住我后在一段时间内，用户无需进行登录操作就可以访问系统资源。在Spring Security中添加记住我功能很简单，大致过程是：当用户勾选了记住我选项并登录成功后，Spring Security会生成一个token标识，然后将该token标识持久化到数据库，并且生成一个与该token相对应的cookie返回给浏览器。当用户过段时间再次访问系统时，如果该cookie没有过期，Spring Security便会根据cookie包含的信息从数据库中获取相应的token信息，然后帮用户自动完成登录操作。

### 自定义退出处理器

https://mrbird.cc/Spring-Security-logout.html

实现LogoutSuccessHandler

security配置

```java
 @Override
protected void configure(HttpSecurity http) throws Exception {
    http.
        ...
        .logout()
    .logoutUrl("/signout")
    // .logoutSuccessUrl("/signout/success")
    .logoutSuccessHandler(logOutSuccessHandler)
    .deleteCookies("JSESSIONID")
}

```

### 自定义权限不足处理器

处理权限不足时候的操作

实现`AccessDeniedHandler`

```java
 @Override
protected void configure(HttpSecurity http) throws Exception {
    http.exceptionHandling()
            .accessDeniedHandler(authenticationAccessDeniedHandler)
        .and()
    ......
}
```
