package com.amsabots.jenzi.fundi_service.services;

import com.amsabots.jenzi.fundi_service.config.ConfigConstants;
import com.amsabots.jenzi.fundi_service.entities.Account;
import com.amsabots.jenzi.fundi_service.entities.Projects;
import com.amsabots.jenzi.fundi_service.repos.AccountRepo;
import com.amsabots.jenzi.fundi_service.repos.ProjectRepo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Configuration
@Slf4j
@AllArgsConstructor
public class CreateNewProject {
    private ObjectMapper objectMapper;
    private AccountRepo accountRepo;
    private ProjectRepo projectRepo;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IncomingPayload implements Serializable {
        private String taskId;
        private String fundiId;
    }

    @RabbitListener(queues = ConfigConstants.FUNDI_NEW_PROJECT_QUEUE)
    public void consumeIncomingProjects(String payload) throws JsonProcessingException {
        IncomingPayload p = objectMapper.readValue(payload, IncomingPayload.class);
        Account a = accountRepo.findAccountByAccountId(p.fundiId).orElse(null);
        if(null != a){
            Projects projects = new Projects();
            projects.setTaskId(p.getTaskId());
            projects.setAccount(a);
            Projects new_project = projectRepo.save(projects);
            //update current account status
            a.setEngaged(true);
            accountRepo.save(a);
            log.info("[new project has been created] [username: {}] [task id: {}] [project Id: {}]", a.getEmail(), p.getTaskId(), new_project.getProjectId());
        }

    }
}
