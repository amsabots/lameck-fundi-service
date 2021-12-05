package com.amsabots.jenzi.fundi_service.contollers;

import com.amsabots.jenzi.fundi_service.entities.Projects;
import com.amsabots.jenzi.fundi_service.enumUtils.ProjectStatus;
import com.amsabots.jenzi.fundi_service.services.ProjectsService;
import com.amsabots.jenzi.fundi_service.utils.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectsService service;

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
}
