package com.lu.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("user")
    public String user(){
        return "permission test success !";
    }
}
