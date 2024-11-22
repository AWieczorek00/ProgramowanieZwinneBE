package pl.demo.zwinne.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.demo.zwinne.model.Project;
import pl.demo.zwinne.model.User;
import pl.demo.zwinne.service.ProjectService;
import pl.demo.zwinne.service.UserService;

@Controller
@Slf4j
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    public void addUserToProject(Long projectId, Long userId) {
        Project project = projectService.getProjectById(projectId);
        User user = userService.getUserById(userId);

        project.getUsers().add(user);
    }

    public void removeUserFromProject(Long projectId, Long userId) {
        Project project = projectService.getProjectById(projectId);
        User user = userService.getUserById(userId);

        project.getUsers().remove(user);
    }
}
