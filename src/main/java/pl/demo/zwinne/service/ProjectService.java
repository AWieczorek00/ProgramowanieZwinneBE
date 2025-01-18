package pl.demo.zwinne.service;

import pl.demo.zwinne.model.Project;
import pl.demo.zwinne.model.ProjectForm;

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
}
