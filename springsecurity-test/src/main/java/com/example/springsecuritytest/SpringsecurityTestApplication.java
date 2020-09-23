package com.example.springsecuritytest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.springsecuritytest.mapper")
public class SpringsecurityTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringsecurityTestApplication.class, args);
	}

}
