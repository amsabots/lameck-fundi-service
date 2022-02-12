package com.amsabots.jenzi.fundi_service.services;

import com.amsabots.jenzi.fundi_service.config.ConfigConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Configuration
@Slf4j
public class CreateNewProject {
    ObjectMapper mapper = new ObjectMapper();
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IncomingPayload implements Serializable {
        private String taskId;
        private String fundiId;
    }

    @RabbitListener(queues = ConfigConstants.FUNDI_NEW_PROJECT_QUEUE)
    public void consumeIncomingProjects(Message payload) {
        byte[] content = payload.getBody();
        log.info("[reason: incoming project details from client side] [info: {}]", content.toString());
    }
}
