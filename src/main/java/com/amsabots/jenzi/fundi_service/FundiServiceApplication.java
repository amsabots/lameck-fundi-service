package com.amsabots.jenzi.fundi_service;

import com.amsabots.jenzi.fundi_service.utils.Commons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@Slf4j
public class FundiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FundiServiceApplication.class, args);
    }


}
