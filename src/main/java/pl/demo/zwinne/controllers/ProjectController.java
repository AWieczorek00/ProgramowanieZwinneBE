package pl.demo.zwinne.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.demo.zwinne.model.Project;
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

    @PostMapping("/{projectId}/user/{userId}")
    public ResponseEntity<Void> addUserToProject(
            @PathVariable Long projectId,
            @PathVariable Long userId) {
        try {
            Project project = projectService.getProjectById(projectId);
            User user = userService.getUserById(userId);

            project.getUsers().add(user);
            projectService.saveProject(project);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error adding user to project", e);

            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{projectId}/user/{userId}")
    public ResponseEntity<Void> removeUserFromProject(
            @PathVariable Long projectId,
            @PathVariable Long userId) {
        try {
            Project project = projectService.getProjectById(projectId);
            User user = userService.getUserById(userId);

            project.getUsers().remove(user);
            projectService.saveProject(project);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error removing user from project", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{projectId}/task")
    public ResponseEntity<Void> addTaskToProject(
            @PathVariable Long projectId,
            @RequestParam String taskName,
            @RequestParam String taskDescription,
            @RequestParam int taskEstimatedTime) {
        try {
            Project project = projectService.getProjectById(projectId);
            int taskOrder = project.getTasks().size() + 1;

            Task task = new Task();
            task.setOrder(taskOrder);
            task.setName(taskName);
            task.setDescription(taskDescription);
            task.setEstimatedTime(taskEstimatedTime);
            task.setProject(project);

            taskService.addTask(task);
            project.getTasks().add(task);
            projectService.saveProject(project);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error adding task to project", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{projectId}/task/{taskId}")
    public ResponseEntity<Void> removeTaskFromProject(
            @PathVariable Long projectId,
            @PathVariable Long taskId) {
        try {
            Project project = projectService.getProjectById(projectId);
            Task task = taskService.getTaskById(taskId);

            project.getTasks().remove(task);
            taskService.deleteTask(taskId);
            projectService.saveProject(project);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error removing task from project", e);
            return ResponseEntity.badRequest().build();
        }
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
