package com.amsabots.jenzi.fundi_service.driver;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@AllArgsConstructor
@Slf4j
public class ProjectHandler {
    private AccountRepo accountRepo;
    private ProjectRepo projectRepo;
    private ObjectMapper objectMapper;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IncomingPayload implements Serializable {
        private String taskId;
        private String fundiId;
    }

    public void handleProjectCreation(String payload) throws JsonProcessingException {
        IncomingPayload p = objectMapper.readValue(payload, IncomingPayload.class);
        log.info("++++++++ [message: trying to create a new project] [task ID: {}] [fundi Id: {}]", p.taskId, p.fundiId);
        Account a = accountRepo.findAccountByAccountId(p.fundiId).orElse(null);
        if (null != a) {
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
