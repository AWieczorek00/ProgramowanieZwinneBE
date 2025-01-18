package pl.demo.zwinne.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.demo.zwinne.model.Task;
import pl.demo.zwinne.service.TaskService;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/api/task")
@PreAuthorize("isAuthenticated()")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/")
    public ResponseEntity<List<Task>> getAllTasks() { return ResponseEntity.ok(taskService.getAll());
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<Task>> getSortedTasks(
            @RequestParam String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam Long projectId){
        List<Task> sortedTasks = taskService.getSortedTasks(sortBy, order, projectId);
        return ResponseEntity.ok(sortedTasks);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(
            @RequestParam String searchText,
            @RequestParam Long projectId) {
        try {
            List<Task> filteredTasks = taskService.searchTasks(searchText, projectId);
            return ResponseEntity.ok(filteredTasks);
        } catch (Exception e) {
            log.error("Error searching tasks", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
