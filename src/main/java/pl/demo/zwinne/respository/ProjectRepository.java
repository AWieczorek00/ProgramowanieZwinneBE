package pl.demo.zwinne.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.demo.zwinne.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository <Project, Long> {
}
