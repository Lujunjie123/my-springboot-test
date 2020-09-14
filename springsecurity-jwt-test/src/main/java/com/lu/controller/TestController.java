package com.lu.controller;

import com.lu.model.entity.User;
import com.lu.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @Autowired
    private AuthService authService;

    //测试逻辑删除
    @PostMapping("/authentication/delete")
    public boolean delete(String username){

        return authService.delete(username);
    }

    @GetMapping("/get")
    public String get(){
        System.getProperties().list(System.out);
        System.out.println("============================================");
        System.out.println(System.getProperty("user.name"));
        System.out.println("============================================");
        System.out.println(System.getProperty("java.library.path"));
        return "执行了";
    }

    @PostMapping("/authentication/register")
    public boolean register(@RequestBody User user){
        return authService.register(user);
    }

    @PostMapping("/authentication/login")
    public String login(String username,String password){
       return authService.login(username,password);
    }

    //带token访问成功，不带不能访问
    // 测试普通权限       //权限不足会报403错误
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/normal/test")
    public String test1() {
        return "ROLE_NORMAL /normal/test接口调用成功！";
    }

    // 测试开发权限
    @PreAuthorize("hasAuthority('ROLE_DEVELOPER')")
    @GetMapping("/admin/test")
    public String test2() {
        return "ROLE_ADMIN /admin/test接口调用成功！";
    }
}







































