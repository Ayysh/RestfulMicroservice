package com.example.restfulservice.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.example")
public class TestRunnerConfiguration {
    public static void main(String args[]) {
        SpringApplication.run(TestRunnerConfiguration.class, args);
    }
}
