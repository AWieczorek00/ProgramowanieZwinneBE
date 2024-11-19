package pl.demo.zwinne.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.demo.zwinne.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
