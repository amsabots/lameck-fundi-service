package com.amsabots.jenzi.fundi_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
@EnableRabbit
public class FundiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FundiServiceApplication.class, args);
    }


}
