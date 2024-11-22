package pl.demo.zwinne.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.demo.zwinne.model.Project;
import pl.demo.zwinne.service.ProjectService;

import java.util.List;

@Controller("/project")
public class ProjectController {

    @Autowired
    private  ProjectService projectService;


    @PostMapping("/add")
    public ResponseEntity< Project> saveProject(@RequestBody Project projectDto) {
        Project project = projectService.saveProject(projectDto);
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable long id) {
        projectService.deleteProject(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Project> updateOrder(@RequestBody Project projectDto) {
        Project project = projectService.updateProject(projectDto);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }
}
