package com.amsabots.jenzi.fundi_service;

import com.amsabots.jenzi.fundi_service.entities.Account;
import com.amsabots.jenzi.fundi_service.services.LocationGenerator;
import com.amsabots.jenzi.fundi_service.utils.Commons;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.geo.Point;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@Slf4j
@EnableEurekaClient
@EnableRabbit
public class FundiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FundiServiceApplication.class, args);
    }


}
