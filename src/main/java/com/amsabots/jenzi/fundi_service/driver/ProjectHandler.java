package com.amsabots.jenzi.fundi_service.driver;

import com.amsabots.jenzi.fundi_service.entities.Account;
import com.amsabots.jenzi.fundi_service.entities.Projects;
import com.amsabots.jenzi.fundi_service.repos.AccountRepo;
import com.amsabots.jenzi.fundi_service.repos.ProjectRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
@Slf4j
public class ProjectHandler {
    private AccountRepo accountRepo;
    private ProjectRepo projectRepo;
    public void handleProjectCreation(String taskId, String fundiId) {
        log.info("++++++++ [message: trying to create a new project] [task ID: {}] [fundi Id: {}]", taskId, fundiId);
        Account a = accountRepo.findAccountByAccountId(fundiId).orElse(null);
        if (null != a) {
            Projects projects = new Projects();
            projects.setTaskId(taskId);
            projects.setAccount(a);
            Projects new_project = projectRepo.save(projects);
            //update current account status
            a.setEngaged(true);
            accountRepo.save(a);
            log.info("[new project has been created] [username: {}] [task id: {}] [project Id: {}]", a.getEmail(), taskId, new_project.getProjectId());
        }else
            log.warn("++++++++++++++++++ [message: Project creation failed] [reason:  Fundi ID provided is invalid]++++++++++++++++++");
    }
}
