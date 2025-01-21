package pl.demo.zwinne.service;

import jakarta.validation.Valid;
import pl.demo.zwinne.dto.TaskDto;
import pl.demo.zwinne.model.Project;
import pl.demo.zwinne.dto.ProjectForm;

import java.util.List;

public interface ProjectService {
    Project addProject(ProjectForm projectForm);
    Project updateProject(ProjectForm projectForm, Long projectID);
    void deleteProject(Long id);
    void saveProject(Project project);
    List<Project> getAll();
    Project getProjectById(Long id);
    List<Project> getSortedProjects(String sortBy, String order);
    List<Project> searchProjects(String searchText);
    void addUserToProject(Long projectId, Long userId);
    void deleteUserFromProject(Long projectId, Long userId);
    void addTaskToProject(Long projectId, @Valid TaskDto taskDto);
    void removeTaskFromProject(Long projectId, Long taskId);
}
