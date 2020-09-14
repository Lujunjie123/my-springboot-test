# my-springboot-test
练习springboot各种功能
- 
springsecurity+jwt+mybatisplus简单的授权实现
**注意点**

> 需要配置密码加密，否则执行到DaoAuthenticationProvider类的additionalAuthenticationChecks中如下代码会出错

```java
	if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
			logger.debug("Authentication failed: password does not match stored value");

			throw new BadCredentialsException(messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.badCredentials",
					"Bad credentials"));
		}
```

`MyWebSecurity`中密码加密配置如下

```
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
```


