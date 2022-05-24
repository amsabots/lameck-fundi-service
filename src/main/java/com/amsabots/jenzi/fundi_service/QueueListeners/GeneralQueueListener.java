package com.amsabots.jenzi.fundi_service.QueueListeners;

import com.amsabots.jenzi.fundi_service.config.ConfigConstants;
import com.amsabots.jenzi.fundi_service.driver.ProjectHandler;
import com.amsabots.jenzi.fundi_service.enumUtils.GeneralPayloadTypeMapper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
@AllArgsConstructor
public class GeneralQueueListener {
    private ObjectMapper objectMapper;
    private ProjectHandler projectHandler;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PayloadOffload {
        private GeneralPayloadTypeMapper action;
        private String payload;
    }

    @RabbitListener(queues = ConfigConstants.JENZI_GENERAL_QUEUE)
    public void consumeIncomingProjects(String payload) throws JsonProcessingException {
        log.warn(payload);
//        PayloadOffload payloadOffload = objectMapper.readValue(payload, PayloadOffload.class);
//        switch (payloadOffload.getAction()) {
//            case NEW_PROJECT:
//                projectHandler.handleProjectCreation(payloadOffload.getPayload());
//                log.info("+++++++++++++ New project handler called ++++++++++++++++");
//                break;
//            default:
//                log.info("[++++++++++ Data sent does not have any known handler provided ++++++++++++]");
//                break;
//        }
    }
}
