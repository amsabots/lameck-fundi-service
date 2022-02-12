package com.amsabots.jenzi.fundi_service.services;

import com.amsabots.jenzi.fundi_service.config.ConfigConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
@Slf4j
public class CreateNewProject {
    ObjectMapper mapper = new ObjectMapper();
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class IncomingPayload{
        private String taskId;
        private String fundiId;
    }

    @RabbitListener(queues = ConfigConstants.FUNDI_NEW_PROJECT_QUEUE)
    public void consumeIncomingProjects(String payload) throws JsonProcessingException {
        IncomingPayload incomingPayload = mapper.readValue(payload, IncomingPayload.class);
        log.info("[reason: incoming project details from client side] [info: {}]", incomingPayload.fundiId);
    }
}
