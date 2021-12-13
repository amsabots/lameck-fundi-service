package com.amsabots.jenzi.fundi_service.services;


import com.amsabots.jenzi.fundi_service.entities.Projects;
import com.amsabots.jenzi.fundi_service.enumUtils.ProjectStatus;
import com.amsabots.jenzi.fundi_service.errorHandlers.CustomResourceNotFound;
import com.amsabots.jenzi.fundi_service.repos.ProjectRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ProjectsService {
    @Autowired
    private ProjectRepo projectRepo;

    public Projects createOrUpdateProject(Projects projects) {
        return projectRepo.save(projects);
    }

    public Projects getProjectById(String id) {
        if (id.length() < 6)
            return projectRepo.findById(Long.valueOf(id)).orElseThrow(() -> new CustomResourceNotFound("The project with the given ID does not exists"));
        return projectRepo.findProjectsByProjectId(id).orElseThrow(() -> new CustomResourceNotFound("The project with the given ID does not exists"));

    }

    public List<Projects> getProjectsByAccountId(long id, Pageable pageable) {
        return projectRepo.findProjectsByAccountId(id, pageable).getContent();

    }

    public List<Projects> getAllProjects(Pageable pageable) {
        return projectRepo.findAll(pageable).getContent();
    }

    public void deleteProject(long id) {
        projectRepo.deleteById(id);
    }

    public List<Projects> getProjectsByStatusAndUserId(Pageable pageable, ProjectStatus projectStatus, long id){
     return projectRepo.findProjectsByProjectIdAndProjectStatus(id, projectStatus, pageable).getContent();
    }


}
