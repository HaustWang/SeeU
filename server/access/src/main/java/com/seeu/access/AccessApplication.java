package com.seeu.access;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.seeu")
public class AccessApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccessApplication.class, args);
    }
}
