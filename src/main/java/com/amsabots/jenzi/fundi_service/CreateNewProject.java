package com.amsabots.jenzi.fundi_service;

import com.amsabots.jenzi.fundi_service.config.ConfigConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreateNewProject {
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class IncomingPayload{
        private String taskId;
        private String fundiId;
    }

    @RabbitListener(queues = ConfigConstants.FUNDI_NEW_PROJECT_QUEUE)
    public void consumeIncomingProjects(IncomingPayload payload){
        log.info("[reason: incoming project details from client side] [info: {}]", payload.toString());
    }
}
