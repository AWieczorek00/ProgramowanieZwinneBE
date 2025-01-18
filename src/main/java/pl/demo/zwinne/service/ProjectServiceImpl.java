package pl.demo.zwinne.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.zwinne.model.Project;
import pl.demo.zwinne.model.ProjectForm;
import pl.demo.zwinne.respository.ProjectRepository;

import java.util.List;
@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Project addProject(ProjectForm projectForm) {
        Project project = new Project(projectForm);
        return projectRepository.save(project);
    }

    @Override
    public Project updateProject(ProjectForm projectForm, Long projectID) {
        Project project = projectRepository.findById(projectID).orElseThrow(() -> new RuntimeException("Can't find project"));
        project.setName(projectForm.getName());
        project.setDescription(projectForm.getDescription());
        project.setDateDefense(projectForm.getDateDefense());
        return projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    @Override
    public List<Project> getAll() { return projectRepository.findAll(); }

    @Override
    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
    }

    @Override
    public List<Project> getSortedProjects(String sortBy, String order) {
        Sort sort = order.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        return projectRepository.findAll(sort);
    }

    @Override
    public List<Project> searchProjects(String searchText) {
        return projectRepository.findByKeyword(searchText.toLowerCase());
    }
}
