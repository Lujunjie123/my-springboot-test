package com.lu.aop;

import com.lu.Entity.SysLog;
import com.lu.annotation.Log;
import com.lu.mapper.LogMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.util.Date;

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









































































































