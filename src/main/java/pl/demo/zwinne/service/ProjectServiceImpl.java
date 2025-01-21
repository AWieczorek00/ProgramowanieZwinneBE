package pl.demo.zwinne.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.zwinne.dto.TaskDto;
import pl.demo.zwinne.model.Project;
import pl.demo.zwinne.dto.ProjectForm;
import pl.demo.zwinne.model.Task;
import pl.demo.zwinne.model.User;
import pl.demo.zwinne.respository.ProjectRepository;

import java.util.List;

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;

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
    public List<Project> getAll() {
        return projectRepository.findAll();
    }

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

    @Override
    public void addUserToProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId).get();
        User user = userService.getUserById(userId);

        project.getUsers().add(user);
        projectRepository.save(project);
    }

    @Override
    public void deleteUserFromProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId).get();
        User user = userService.getUserById(userId);

        project.getUsers().remove(user);
        projectRepository.save(project);
    }

    @Override
    public void addTaskToProject(Long projectId, TaskDto taskDto) {
        Project project = projectRepository.findById(projectId).get();
        int taskOrder = project.getTasks().size() + 1;

        Task task = new Task();
        task.setOrder(taskOrder);
        task.setName(taskDto.getTaskName());
        task.setDescription(taskDto.getTaskDescription());
        task.setEstimatedTime(taskDto.getTaskEstimatedTime());
        task.setProject(project);

        taskService.addTask(task);
        project.getTasks().add(task);
        projectRepository.save(project);
    }

    @Override
    public void removeTaskFromProject(Long projectId, Long taskId) {
        Project project = projectRepository.findById(projectId).get();
        Task task = taskService.getTaskById(taskId);
        project.getTasks().remove(task);
        taskService.deleteTask(task);
        projectRepository.save(project);
    }
}
