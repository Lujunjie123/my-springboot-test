## Spring Security+OAuth2实现单点登陆

创建一个maven多模块项目，包含认证服务器和两个客户端。

### 认证服务器

#### pom依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-oauth2</artifactId>
</dependency>
```

#### Spring Security配置类

```java
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().and().authorizeRequests().anyRequest().authenticated();
    }
}
```

#### 自定义用户登录认证

```java
@Component
public class UserDetailService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if( !"codesheep".equals(username) )
            throw new UsernameNotFoundException("用户" + username + "不存在" );

        return new User(username, passwordEncoder.encode("123456"),
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_NORMAL,ROLE_MEDIUM"));
    }
}
```

#### 认证服务器配置

使用默认的Spring Security登录页面来进行认证，所以需要开启`authorization_code`类型认证支持。	

**注意**：要配置重定向地址：否则会报非法请求，至少需要一个重定向URL被注册到client

```java
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("userDetailService")
    private UserDetailsService userDetailsService;

    //使用JWT作为令牌
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("test_key");
        return converter;
    }
    @Bean
    public JwtTokenStore jwtTokenStore(){
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    //定义了两个客户端应用的通行证	需要配置重定向
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("lu1").secret(passwordEncoder.encode("123456"))
                .authorizedGrantTypes("refresh_token","authorization_code")
                .accessTokenValiditySeconds(3600)
                .scopes("all").autoApprove(false)
                .redirectUris("http://localhost:8001/login")
                .and()
                .withClient("lu2").secret(passwordEncoder.encode("123456"))
                .authorizedGrantTypes("refresh_token","authorization_code")
                .accessTokenValiditySeconds(7200)
                .scopes("all").autoApprove(true)		//自动授权
                .redirectUris("http://localhost:8002/login");
    }

    // 获取密钥需要身份认证
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("isAuthenticated()");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(jwtTokenStore()).accessTokenConverter(jwtAccessTokenConverter())
                .userDetailsService(userDetailsService);
    }
}
```

#### yaml

```yaml
server:
  servlet:
    context-path: /server
  port: 8080
```

### 客户端配置

主启动类增加@EnableOAuth2Sso注解

#### yaml

`security.oauth2.client.client-id`和`security.oauth2.client.client-secret`指定了客户端id和密码，这里和认证服务器里配置的client一致，`user-authorization-uri`指定为认证服务器的`/oauth/authorize`地址，`access-token-uri`指定为认证服务器的`/oauth/token`地址，`jwt.key-uri`指定为认证服务器的`/oauth/token_key`地址。

```yaml
auth-server: http://localhost:8080/server
server:
  port: 8001
security:
  oauth2:
    client:
      client-id: lu1
      client-secret: 123456
      access-token-uri: ${auth-server}/oauth/token
      user-authorization-uri: ${auth-server}/oauth/authorize
    resource:
      jwt:
        key-uri: ${auth-server}/oauth/token_ke
```

#### 测试

```java
@RestController
public class TestController {

    @GetMapping("user")
    public String user(){
        return "permission test success !";
    }
}
```

先启动认证服务器再启动客户端

访问localhost:8001/user