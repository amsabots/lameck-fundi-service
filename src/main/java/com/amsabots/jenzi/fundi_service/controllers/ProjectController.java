package com.amsabots.jenzi.fundi_service.controllers;

import com.amsabots.jenzi.fundi_service.driver.ProjectHandler;
import com.amsabots.jenzi.fundi_service.entities.Projects;
import com.amsabots.jenzi.fundi_service.enumUtils.ProjectStatus;
import com.amsabots.jenzi.fundi_service.errorHandlers.CustomResourceNotFound;
import com.amsabots.jenzi.fundi_service.repos.ProjectRepo;
import com.amsabots.jenzi.fundi_service.services.ProjectsService;
import com.amsabots.jenzi.fundi_service.utils.ResponseObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.print.attribute.standard.Media;
import javax.ws.rs.Path;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/projects")
@Slf4j
@AllArgsConstructor
public class ProjectController {


    private ProjectsService service;
    private WebClient webClient;
    private ProjectRepo repo;
    private ProjectHandler projectHandler;

    /*
     * ============  ADMIN ACCESS ONLY =====================
     * The person accessing this endpoint should explicitly provide defined privileges and should be availed implicitly by spring boot security context
     * This functionality should be enabled during the initial setup of spring security
     *
     * */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject<Projects>> getAllProjects(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> pageSize) {
        int current_page = page.orElse(0);
        int page_size = pageSize.orElse(20);
        List<Projects> projects = service.getAllProjects(PageRequest.of(current_page, page_size));
        return ResponseEntity.ok().body(new ResponseObject<Projects>(projects, page_size, current_page));
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject<Projects>>
    getAllProjectsByAccountId(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> pageSize, @PathVariable long id) {
        int current_page = page.orElse(0);
        int page_size = pageSize.orElse(20);
        List<Projects> projects = service.getProjectsByAccountId(id, PageRequest.of(current_page, page_size));
        return ResponseEntity.ok().body(new ResponseObject<Projects>(projects, page_size, current_page));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Projects> createProject(@RequestBody Projects projects) {
        return ResponseEntity.ok(service.createOrUpdateProject(projects));
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateProject(@PathVariable long id, @RequestBody Projects projects) {
        service.getProjectById(String.valueOf(id));
        projects.setId(id);
        service.createOrUpdateProject(projects);
        return ResponseEntity.ok().body("{\"message\":\"The project details have been updated successfully\"}");
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> removeProject(@PathVariable long id) {
        service.deleteProject(id);
        return ResponseEntity.ok().body("{\"message\":\"The project has been deleted successfully from your records\"}");
    }

    //select projects by user id
    @GetMapping(path = "/project-status/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseObject<Projects>> findAllProjectsByStatusAndUserId(@PathVariable long id,
                                                                                     @RequestParam Optional<ProjectStatus> projectstatus,
                                                                                     @RequestParam Optional<Integer> pageSize,
                                                                                     @RequestParam Optional<Integer> page) {
        ProjectStatus pr_status = projectstatus.orElse(ProjectStatus.COMPLETE);
        int page_size = pageSize.orElse(10), pg = page.orElse(0);
        Pageable pageable = PageRequest.of(pg, page_size);
        List<Projects> projects = service.getProjectsByStatusAndUserId(pageable, pr_status, id);
        return ResponseEntity.ok().body(new ResponseObject<>(projects, page_size, pg));

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/details/{jobId}")
    public ResponseEntity<Object> getProjectDetailsFromClient(@PathVariable long jobId) {
        Flux<Object> tweetFlux = webClient
                .get()
                .uri("http://localhost:27900/client/api/jobs/" + jobId)
                .retrieve()
                .bodyToFlux(Object.class);
        return ResponseEntity.ok().body(tweetFlux);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/client/{id}")
    public ResponseEntity<List<Projects>> findAllProjectsByTaskId(@PathVariable String id) {
        return ResponseEntity.ok().body(repo.findAllByTaskId(id)
                .orElseThrow(() -> new CustomResourceNotFound("Record details not available at this moment.")));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/start/{fundiId}/{taskId}")
    public ResponseEntity<String> createProject(@PathVariable String fundiId, @PathVariable String taskId) {
        projectHandler.handleProjectCreation(taskId, fundiId);
        return ResponseEntity.ok("\"message\":\"Project successfully created\"");
    }
}
