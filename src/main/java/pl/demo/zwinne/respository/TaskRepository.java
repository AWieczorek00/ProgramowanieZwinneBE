package pl.demo.zwinne.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.demo.zwinne.model.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t " +
            "WHERE LOWER(t.name) LIKE %:keyword% " +
            "OR LOWER(t.description) LIKE %:keyword% " +
            "OR LOWER(CAST(t.estimatedTime AS string)) LIKE %:keyword% " +
            "AND t.project.id = :projectId")
    List<Task> findByKeyword(String keyword, Long projectId);
}
