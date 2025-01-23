package pl.demo.zwinne.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.demo.zwinne.dto.TaskDto;
import pl.demo.zwinne.model.Project;
import pl.demo.zwinne.dto.ProjectForm;
import pl.demo.zwinne.model.Task;
import pl.demo.zwinne.model.User;
import pl.demo.zwinne.service.ProjectService;
import pl.demo.zwinne.service.TaskService;
import pl.demo.zwinne.service.UserService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/project")
@PreAuthorize("isAuthenticated()")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;


    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','TEACHER')")
    @PostMapping("/")
    public ResponseEntity<Project> addProject(@Valid @RequestBody ProjectForm projectForm){
        return ResponseEntity.ok(projectService.addProject(projectForm));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','TEACHER')")
    @PatchMapping("/{id}")
    public ResponseEntity<Project> updateProject(@Valid @RequestBody ProjectForm projectForm, @PathVariable(name = "id") Long id){
        return ResponseEntity.ok(projectService.updateProject(projectForm, id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','TEACHER')")
    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable(name = "id") Long id){
        projectService.deleteProject(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','TEACHER', 'STUDENT')")
    @GetMapping("/{projectId}/user")
    public ResponseEntity<List<User>> getAllUsersFromProject(
            @PathVariable Long projectId
    ){
        return ResponseEntity.ok(projectService.getProjectById(projectId).getUsers());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','TEACHER')")
    @PostMapping("/{projectId}/user/{userId}")
    public ResponseEntity<Void> addUserToProject(
            @PathVariable Long projectId,
            @PathVariable Long userId) {
        projectService.addUserToProject(projectId, userId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','TEACHER')")
    @DeleteMapping("/{projectId}/user/{userId}")
    public ResponseEntity<Void> removeUserFromProject(
            @PathVariable Long projectId,
            @PathVariable Long userId) {
        projectService.deleteUserFromProject(projectId, userId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','TEACHER')")
    @PostMapping("/{projectId}/task")
    public ResponseEntity<Void> addTaskToProject(
            @PathVariable Long projectId,
            @Valid @RequestBody TaskDto taskDto) {
        projectService.addTaskToProject(projectId, taskDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','TEACHER')")
    @DeleteMapping("/{projectId}/task/{taskId}")
    public ResponseEntity<Void> removeTaskFromProject(
            @PathVariable Long projectId,
            @PathVariable Long taskId) {
        projectService.removeTaskFromProject(projectId, taskId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<Project>> getSortedProjects(
            @RequestParam String sortBy,
            @RequestParam(defaultValue = "asc") String order) {
        List<Project> sortedProjects = projectService.getSortedProjects(sortBy, order);
        return ResponseEntity.ok(sortedProjects);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Project>> searchProjects(@RequestParam String searchText) {
        try {
            List<Project> filteredProjects = projectService.searchProjects(searchText);
            return ResponseEntity.ok(filteredProjects);
        } catch (Exception e) {
            log.error("Error searching projects", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
