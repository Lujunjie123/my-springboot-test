package com.lu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

@SpringBootApplication
@EnableOAuth2Sso
public class Oauth2Clien2Main {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2Clien2Main.class,args);
    }
}