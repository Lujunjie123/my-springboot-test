## 过滤器

### 实现javax.servlet.Filter

```java
public class TimeFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("过滤器初始化");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("开始执行过滤器");
        Long start = new Date().getTime();
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("【过滤器】耗时 " + (new Date().getTime() - start));
        System.out.println("结束执行过滤器");
    }

    @Override
    public void destroy() {
        System.out.println("过滤器销毁");
    }
}
```

### 配置方法一:

`@WebFilter`注解的`urlPatterns`属性配置了哪些请求可以进入该过滤器，`/*`表示所有请求

```java
@Component
@WebFilter(urlPatterns = {"/*"})
public class TimeFilter implements Filter {
   ...
}
```

### 配置方法二:

```java
@Configuration
public class WebConfig {
    @Bean
    public FilterRegistrationBean timeFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        TimeFilter timeFilter = new TimeFilter();
        filterRegistrationBean.setFilter(timeFilter);

        List<String> urlList = new ArrayList<>();
        urlList.add("/*");

        filterRegistrationBean.setUrlPatterns(urlList);
        return filterRegistrationBean;
    }
}
```

## 拦截器

### 实现HandlerInterceptor接口:

`org.springframework.web.servlet.HandlerInterceptor`

`preHandle`方法在处理拦截之前执行，`postHandle`只有当被拦截的方法没有抛出异常成功时才会处理，`afterCompletion`方法无论被拦截的方法抛出异常与否都会执行。

注意：只有当preHandle方法返回true才会执行后续操作

```java
public class TimeInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        System.out.println("处理拦截之前");
        httpServletRequest.setAttribute("startTime", new Date().getTime());
        System.out.println(((HandlerMethod) o).getBean().getClass().getName());
        System.out.println(((HandlerMethod) o).getMethod().getName());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println("开始处理拦截");
        Long start = (Long) httpServletRequest.getAttribute("startTime");
        System.out.println("【拦截器】耗时 " + (new Date().getTime() - start));
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        System.out.println("处理拦截之后");
        Long start = (Long) httpServletRequest.getAttribute("startTime");
        System.out.println("【拦截器】耗时 " + (new Date().getTime() - start));
        System.out.println("异常信息 " + e);
    }
}
```

### 注册拦截器:

通过`InterceptorRegistry`

```java
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private TimeInterceptor timeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(timeInterceptor);
    }
}
```

过滤器要先于拦截器执行，晚于拦截器结束。下图很好的描述了它们的执行时间区别

![过滤器拦截器执行顺序](https://github.com/Lujunjie123/my-springboot-test/tree/master/springboot-filter-interceptor-test/执行顺序.png)

## AOP记录用户日志

### 依赖

```xml
<!-- aop依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

### 定义注解

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    String value() default "";
}
```

### 定义切面

定义一个LogAspect类，使用`@Aspect`标注让其成为一个切面，切点为使用`@Log`注解标注的方法，使用`@Around`环绕通知：

```java
@Aspect
@Component
public class LogAspect {

    @Autowired
    LogMapper logMapper;

    @Pointcut("@annotation(com.lu.annotation.Log)")
    public void pointcut() {};

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point){
        Object proceed = null;
        long begintime = new Date().getTime();
        try {
            proceed  = point.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        //执行时长
        long time = new Date().getTime() - begintime;
        saveLog(point,time);
        return proceed;
    }

    private void saveLog(ProceedingJoinPoint point, long time) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        SysLog sysLog = new SysLog();
        //获取类名
        String className = point.getTarget().getClass().getName();
        Log annotation = signature.getMethod().getAnnotation(Log.class);
        //设置操作
        if(annotation!=null){
            sysLog.setOperation(annotation.value());
        }
        //获取参数值
        Object[] args = point.getArgs();
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        //获取参数
        String[] parameterNames = u.getParameterNames(signature.getMethod());
        if(args != null && parameterNames != null){
            String params = "";
            for (int i = 0; i < parameterNames.length; i++) {
                params += parameterNames[i]+": "+args[i]+"  ";
            }
            sysLog.setParams(params);
        }
        //获取方法名
        String methodName = signature.getName();
        sysLog.setClassName(className);
        sysLog.setMethodName(methodName);
        sysLog.setTime(time);
        sysLog.setUsername("张三");
        logMapper.insert(sysLog);
        System.out.println("-------------保存日志完成");
    }
}
```

### 表结构

```sql
CREATE TABLE `log` (
  `id` char(19) NOT NULL,
  `username` varchar(50) NOT NULL,
  `operation` varchar(50) NOT NULL,
  `class_name` varchar(50) NOT NULL,
  `method_name` varchar(50) NOT NULL,
  `params` varchar(50) DEFAULT NULL,
  `time` bigint(50) NOT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
```

### 实体类

```java
@Data
@TableName("log")
@AllArgsConstructor
@NoArgsConstructor
public class SysLog {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String username;

    private String operation; //日志语句

    private String className;  //类名

    private String methodName;  //方法名

    private String params;      //方法参数

    private Long time;          //执行时间

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;
}
```

### 测试

```java
@Log("执行方法一")
@GetMapping("/one")
public boolean testLog1(){
    return true;
}

@Log("执行方法二")
@GetMapping("/two")
public boolean testLog2(int a,String b){
    return true;
}
```

