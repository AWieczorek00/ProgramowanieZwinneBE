package pl.demo.zwinne;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.zwinne.controllers.ProjectController;
import pl.demo.zwinne.model.Project;
import pl.demo.zwinne.model.User;
import pl.demo.zwinne.respository.ProjectRepository;
import pl.demo.zwinne.respository.UserRepository;

import java.util.ArrayList;

@Transactional
@SpringBootTest
class ProgramowanieZwinneBeApplicationTests {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
	@Autowired
	private ProjectController projectController;

	@Test
	void contextLoads() {
	}

	@Test
	void addUsersToProject() {
		Project project = new Project();
		project.setName("test");
		project.setUsers(new ArrayList<>());

		projectRepository.save(project);

		User user1 = new User();
		user1.setName("test1");
		user1.setEmail("test@test.com");
		user1.setSurname("test");
		user1.setIndexNumber("1");
		user1.setStationary(true);

		userRepository.save(user1);

		User user2 = new User();
		user2.setName("test2");
		user2.setEmail("test@test.com");
		user2.setSurname("test");
		user2.setIndexNumber("1");
		user2.setStationary(true);

		userRepository.save(user2);

		projectController.addUserToProject(project.getId(), user1.getId());
		projectController.addUserToProject(project.getId(), user2.getId());

		System.out.println("USERS IN PROJECT: " + project.getUsers());

		projectController.removeUserFromProject(project.getId(), user1.getId());

		System.out.println("USERS IN PROJECT: " + project.getUsers());
	}
}
