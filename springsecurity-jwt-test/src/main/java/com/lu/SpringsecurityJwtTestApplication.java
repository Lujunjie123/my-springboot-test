package com.lu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.lu.mapper")
public class SpringsecurityJwtTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringsecurityJwtTestApplication.class, args);
	}

}
