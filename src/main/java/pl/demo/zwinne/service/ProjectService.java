package pl.demo.zwinne.service;

import co.elastic.clients.util.DateTime;
import org.springframework.stereotype.Service;
import pl.demo.zwinne.respository.ProjectRepository;
import pl.demo.zwinne.model.Project;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public void deleteProject(long id) {
        projectRepository.deleteById(id);
    }

    public Project updateProject(Project project) {
        return projectRepository.findById(project.getId())
                .map(projectUpdate -> {
                    projectUpdate.setName(project.getName());
                    projectUpdate.setDateModify(LocalDateTime.now());
                    projectUpdate.setTasks(project.getTasks());
                    return projectRepository.save(projectUpdate);
                })
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

}
