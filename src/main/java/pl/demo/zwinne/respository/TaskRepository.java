package pl.demo.zwinne.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.demo.zwinne.model.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t " +
            "WHERE (LOWER(t.name) LIKE %?1 " +
            "OR LOWER(t.description) LIKE %?1 " +
            "OR LOWER(CAST(t.estimatedTime AS string)) LIKE %?1 )" +
            "AND t.project.id = ?2")
    List<Task> findByKeywordAndProjectId(String keyword, Long projectId);
}
