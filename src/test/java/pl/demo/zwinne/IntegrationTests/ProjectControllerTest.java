package pl.demo.zwinne.IntegrationTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.demo.zwinne.Enum.RoleEnum;
import pl.demo.zwinne.dto.ProjectForm;
import pl.demo.zwinne.dto.TaskDto;
import pl.demo.zwinne.model.Project;
import pl.demo.zwinne.model.Role;
import pl.demo.zwinne.model.Task;
import pl.demo.zwinne.model.User;
import pl.demo.zwinne.respository.RoleRepository;
import pl.demo.zwinne.respository.TaskRepository;
import pl.demo.zwinne.service.ProjectService;
import pl.demo.zwinne.service.TaskService;
import pl.demo.zwinne.service.UserService;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProjectControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TaskService taskService;

    @Test
    @WithMockUser(username = "test", roles = {"TEACHER"})
    public void addProjectAsTeacher() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());
        String json = mapper.writeValueAsString(projectForm);
        mvc.perform(post("/api/project/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String project_json = result.getResponse().getContentAsString();
                    Project project = mapper.readValue(project_json, Project.class);
                    projectService.deleteProject(project.getId());
                });
    }

    @Test
    @WithMockUser(username = "test", roles = {"STUDENT"})
    public void addProjectAsStudent() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());
        String json = mapper.writeValueAsString(projectForm);
        mvc.perform(post("/api/project/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "test", roles = {""})
    public void addProjectAsGuest() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());
        String json = mapper.writeValueAsString(projectForm);
        mvc.perform(post("/api/project/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "test", roles = {"TEACHER"})
    public void updateProjectAsTeacher() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());

        Project project = new Project(projectForm);
        projectService.saveProject(project);

        projectForm.setName("Updated Test Project");
        String json = mapper.writeValueAsString(projectForm);

        mvc.perform(patch("/api/project/" + project.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(projectForm.getName())))
                .andDo(result -> {
                    projectService.deleteProject(project.getId());
                });
    }

    @Test
    @WithMockUser(username = "test", roles = {"STUDENT"})
    public void updateProjectAsStudent() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());

        Project project = new Project(projectForm);
        projectService.saveProject(project);

        projectForm.setName("Updated Test Project");
        String json = mapper.writeValueAsString(projectForm);

        mvc.perform(patch("/api/project/" + project.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(result -> {
                    projectService.deleteProject(project.getId());
                });
    }

    @Test
    @WithMockUser(username = "test", roles = {""})
    public void updateProjectAsGuest() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());

        Project project = new Project(projectForm);
        projectService.saveProject(project);

        projectForm.setName("Updated Test Project");
        String json = mapper.writeValueAsString(projectForm);

        mvc.perform(patch("/api/project/" + project.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(result -> {
                    projectService.deleteProject(project.getId());
                });
    }

    @Test
    @WithMockUser(username = "test", roles = {"STUDENT"})
    public void deleteProjectAsStudent() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());

        Project project = new Project(projectForm);
        projectService.saveProject(project);

        mvc.perform(delete("/api/project/" + project.getId()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "test", roles = {"TEACHER"})
    public void deleteProjectAsTeacher() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());

        Project project = new Project(projectForm);
        projectService.saveProject(project);

        mvc.perform(delete("/api/project/" + project.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", roles = {""})
    public void deleteProjectAsGuest() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());

        Project project = new Project(projectForm);
        projectService.saveProject(project);

        mvc.perform(delete("/api/project/" + project.getId()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "test", roles = {"TEACHER"})
    public void getAllUsersFromProjectAsTeacher() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());

        Project project = new Project(projectForm);

        User user = new User();
        user.setName("Test user");
        user.setSurname("Test user");
        user.setEmail("test@gmail.com");
        user.setPassword("abcd");
        user.setIndexNumber("123456789");
        user.setStationary(false);

        Role role = new Role();
        role.setName(RoleEnum.TEST);
        role.setDescription("\"TEST ROLE\"");
        roleRepository.save(role);

        user.setRole(role);
        userService.addUser(user);

        project.getUsers().add(user);
        projectService.saveProject(project);

        mvc.perform(get("/api/project/" + project.getId() + "/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Test user")))
                .andDo(result -> {
                    projectService.deleteProject(project.getId());
                    userService.deleteUser(user.getId());
                    roleRepository.delete(role);
                });
    }

    @Test
    @WithMockUser(username = "test", roles = {"STUDENT"})
    public void getAllUsersFromProjectAsStudent() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());

        Project project = new Project(projectForm);

        User user = new User();
        user.setName("Test user");
        user.setSurname("Test user");
        user.setEmail("test@gmail.com");
        user.setPassword("abcd");
        user.setIndexNumber("123456789");
        user.setStationary(false);

        Role role = new Role();
        role.setName(RoleEnum.TEST);
        role.setDescription("\"TEST ROLE\"");
        roleRepository.save(role);

        user.setRole(role);
        userService.addUser(user);

        project.getUsers().add(user);
        projectService.saveProject(project);

        mvc.perform(get("/api/project/" + project.getId() + "/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Test user")))
                .andDo(result -> {
                    projectService.deleteProject(project.getId());
                    userService.deleteUser(user.getId());
                    roleRepository.delete(role);
                });
    }

    @Test
    @WithMockUser(username = "test", roles = {""})
    public void getAllUsersFromProjectAsGuest() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());

        Project project = new Project(projectForm);

        User user = new User();
        user.setName("Test user");
        user.setSurname("Test user");
        user.setEmail("test@gmail.com");
        user.setPassword("abcd");
        user.setIndexNumber("123456789");
        user.setStationary(false);

        Role role = new Role();
        role.setName(RoleEnum.TEST);
        role.setDescription("\"TEST ROLE\"");
        roleRepository.save(role);

        user.setRole(role);
        userService.addUser(user);

        project.getUsers().add(user);
        projectService.saveProject(project);

        mvc.perform(get("/api/project/" + project.getId() + "/user"))
                .andExpect(status().is4xxClientError())
                .andDo(result -> {
                    projectService.deleteProject(project.getId());
                    userService.deleteUser(user.getId());
                    roleRepository.delete(role);
                });
    }

    @Test
    @WithMockUser(username = "test", roles = {"TEACHER"})
    public void addUserToProjectAsTeacher() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());

        Project project = new Project(projectForm);

        User user = new User();
        user.setName("Test user");
        user.setSurname("Test user");
        user.setEmail("test@gmail.com");
        user.setPassword("abcd");
        user.setIndexNumber("123456789");
        user.setStationary(false);

        Role role = new Role();
        role.setName(RoleEnum.TEST);
        role.setDescription("\"TEST ROLE\"");
        roleRepository.save(role);

        user.setRole(role);
        userService.addUser(user);
        projectService.saveProject(project);

        mvc.perform(post("/api/project/" + project.getId() + "/user/" + user.getId()))
                .andExpect(status().isOk())
                .andDo(result -> {
                    projectService.deleteProject(project.getId());
                    userService.deleteUser(user.getId());
                    roleRepository.delete(role);
                });
    }

    @Test
    @WithMockUser(username = "test", roles = {"STUDENT"})
    public void addUserToProjectAsStudent() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());

        Project project = new Project(projectForm);

        User user = new User();
        user.setName("Test user");
        user.setSurname("Test user");
        user.setEmail("test@gmail.com");
        user.setPassword("abcd");
        user.setIndexNumber("123456789");
        user.setStationary(false);

        Role role = new Role();
        role.setName(RoleEnum.TEST);
        role.setDescription("\"TEST ROLE\"");
        roleRepository.save(role);

        user.setRole(role);
        userService.addUser(user);
        projectService.saveProject(project);

        mvc.perform(post("/api/project/" + project.getId() + "/user/" + user.getId()))
                .andExpect(status().is4xxClientError())
                .andDo(result -> {
                    projectService.deleteProject(project.getId());
                    userService.deleteUser(user.getId());
                    roleRepository.delete(role);
                });
    }

    @Test
    @WithMockUser(username = "test", roles = {""})
    public void addUserToProjectAsGuest() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());

        Project project = new Project(projectForm);

        User user = new User();
        user.setName("Test user");
        user.setSurname("Test user");
        user.setEmail("test@gmail.com");
        user.setPassword("abcd");
        user.setIndexNumber("123456789");
        user.setStationary(false);

        Role role = new Role();
        role.setName(RoleEnum.TEST);
        role.setDescription("\"TEST ROLE\"");
        roleRepository.save(role);

        user.setRole(role);
        userService.addUser(user);
        projectService.saveProject(project);

        mvc.perform(post("/api/project/" + project.getId() + "/user/" + user.getId()))
                .andExpect(status().is4xxClientError())
                .andDo(result -> {
                    projectService.deleteProject(project.getId());
                    userService.deleteUser(user.getId());
                    roleRepository.delete(role);
                });
    }

    @Test
    @WithMockUser(username = "test", roles = {"TEACHER"})
    public void removeUserFromProjectAsTeacher() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());

        Project project = new Project(projectForm);

        User user = new User();
        user.setName("Test user");
        user.setSurname("Test user");
        user.setEmail("test@gmail.com");
        user.setPassword("abcd");
        user.setIndexNumber("123456789");
        user.setStationary(false);

        Role role = new Role();
        role.setName(RoleEnum.TEST);
        role.setDescription("\"TEST ROLE\"");
        roleRepository.save(role);

        user.setRole(role);
        userService.addUser(user);
        project.getUsers().add(user);
        projectService.saveProject(project);

        mvc.perform(delete("/api/project/" + project.getId() + "/user/" + user.getId()))
                .andExpect(status().isOk())
                .andDo(result -> {
                    projectService.deleteProject(project.getId());
                    userService.deleteUser(user.getId());
                    roleRepository.delete(role);
                });
    }

    @Test
    @WithMockUser(username = "test", roles = {"STUDENT"})
    public void removeUserFromProjectAsStudent() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());

        Project project = new Project(projectForm);

        User user = new User();
        user.setName("Test user");
        user.setSurname("Test user");
        user.setEmail("test@gmail.com");
        user.setPassword("abcd");
        user.setIndexNumber("123456789");
        user.setStationary(false);

        Role role = new Role();
        role.setName(RoleEnum.TEST);
        role.setDescription("\"TEST ROLE\"");
        roleRepository.save(role);

        user.setRole(role);
        userService.addUser(user);
        project.getUsers().add(user);
        projectService.saveProject(project);

        mvc.perform(delete("/api/project/" + project.getId() + "/user/" + user.getId()))
                .andExpect(status().is4xxClientError())
                .andDo(result -> {
                    projectService.deleteProject(project.getId());
                    userService.deleteUser(user.getId());
                    roleRepository.delete(role);
                });
    }

    @Test
    @WithMockUser(username = "test", roles = {""})
    public void removeUserFromProjectAsGuest() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());

        Project project = new Project(projectForm);

        User user = new User();
        user.setName("Test user");
        user.setSurname("Test user");
        user.setEmail("test@gmail.com");
        user.setPassword("abcd");
        user.setIndexNumber("123456789");
        user.setStationary(false);

        Role role = new Role();
        role.setName(RoleEnum.TEST);
        role.setDescription("\"TEST ROLE\"");
        roleRepository.save(role);

        user.setRole(role);
        userService.addUser(user);
        project.getUsers().add(user);
        projectService.saveProject(project);

        mvc.perform(delete("/api/project/" + project.getId() + "/user/" + user.getId()))
                .andExpect(status().is4xxClientError())
                .andDo(result -> {
                    projectService.deleteProject(project.getId());
                    userService.deleteUser(user.getId());
                    roleRepository.delete(role);
                });
    }

    @Test
    @WithMockUser(username = "test", roles = {"TEACHER"})
    public void addTaskToProjectAsTeacher() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());

        Project project = new Project(projectForm);
        projectService.saveProject(project);

        TaskDto taskDto = new TaskDto();
        taskDto.setTaskName("Test task");
        taskDto.setTaskDescription("Test task description");
        taskDto.setTaskEstimatedTime(2);

        String json = mapper.writeValueAsString(taskDto);

        mvc.perform(post("/api/project/" + project.getId() + "/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(result -> {
                    projectService.deleteProject(project.getId());
                });
    }

    @Test
    @WithMockUser(username = "test", roles = {"STUDENT"})
    public void addTaskToProjectAsStudent() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());

        Project project = new Project(projectForm);
        projectService.saveProject(project);

        TaskDto taskDto = new TaskDto();
        taskDto.setTaskName("Test task");
        taskDto.setTaskDescription("Test task description");
        taskDto.setTaskEstimatedTime(2);

        String json = mapper.writeValueAsString(taskDto);

        mvc.perform(post("/api/project/" + project.getId() + "/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(result -> {
                    projectService.deleteProject(project.getId());
                });
    }

    @Test
    @WithMockUser(username = "test", roles = {""})
    public void addTaskToProjectAsGuest() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());

        Project project = new Project(projectForm);
        projectService.saveProject(project);

        TaskDto taskDto = new TaskDto();
        taskDto.setTaskName("Test task");
        taskDto.setTaskDescription("Test task description");
        taskDto.setTaskEstimatedTime(2);

        String json = mapper.writeValueAsString(taskDto);

        mvc.perform(post("/api/project/" + project.getId() + "/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(result -> {
                    projectService.deleteProject(project.getId());
                });
    }

    @Test
    @WithMockUser(username = "test", roles = {"TEACHER"})
    public void removeTaskToProjectAsTeacher() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());

        Project project = new Project(projectForm);
        projectService.saveProject(project);

        Task task = new Task();
        task.setName("Test task");
        task.setDescription("Test task description");
        task.setEstimatedTime(2);
        task.setOrder(0);
        task.setProject(project);
        taskService.addTask(task);

        mvc.perform(delete("/api/project/" + project.getId() + "/task/"+task.getId()))
                .andExpect(status().isOk())
                .andDo(result -> {
                    projectService.deleteProject(project.getId());
                });
    }

    @Test
    @WithMockUser(username = "test", roles = {"STUDENT"})
    public void removeTaskToProjectAsStudent() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());

        Project project = new Project(projectForm);
        projectService.saveProject(project);

        Task task = new Task();
        task.setName("Test task");
        task.setDescription("Test task description");
        task.setEstimatedTime(2);
        task.setOrder(0);
        task.setProject(project);
        taskService.addTask(task);

        mvc.perform(delete("/api/project/" + project.getId() + "/task/"+task.getId()))
                .andExpect(status().is4xxClientError())
                .andDo(result -> {
                    projectService.deleteProject(project.getId());
                });
    }

    @Test
    @WithMockUser(username = "test", roles = {""})
    public void removeTaskToProjectAsGuest() throws Exception {
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("Test project description");
        projectForm.setDateDefense(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());

        Project project = new Project(projectForm);
        projectService.saveProject(project);

        Task task = new Task();
        task.setName("Test task");
        task.setDescription("Test task description");
        task.setEstimatedTime(2);
        task.setOrder(0);
        task.setProject(project);
        taskService.addTask(task);

        mvc.perform(delete("/api/project/" + project.getId() + "/task/"+task.getId()))
                .andExpect(status().is4xxClientError())
                .andDo(result -> {
                    projectService.deleteProject(project.getId());
                });
    }
}
