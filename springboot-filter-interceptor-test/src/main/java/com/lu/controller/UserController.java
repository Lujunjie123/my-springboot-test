package com.lu.controller;

import com.lu.annotation.Log;
import com.lu.mapper.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping("{id:\\d+}")
    public void get(@PathVariable String id) {
        System.out.println(id);
//        throw new RuntimeException("user not exist");
    }

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
}
































