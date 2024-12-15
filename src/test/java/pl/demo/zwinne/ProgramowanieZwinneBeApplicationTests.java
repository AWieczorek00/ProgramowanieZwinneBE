package pl.demo.zwinne;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.zwinne.controllers.ProjectController;
import pl.demo.zwinne.respository.ProjectRepository;

@Transactional
@SpringBootTest
class ProgramowanieZwinneBeApplicationTests {

    @Autowired
    private ProjectRepository projectRepository;

	@Autowired
	private ProjectController projectController;

	@Test
	void contextLoads() {
	}
}
