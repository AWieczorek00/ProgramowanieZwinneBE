package pl.demo.zwinne.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.demo.zwinne.model.Project;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository <Project, Long> {
    @Query("SELECT p FROM Project p " +
            "WHERE LOWER(p.name) LIKE %:keyword% " +
            "OR LOWER(p.description) LIKE %:keyword% " +
            "OR LOWER(CAST(p.dateCreate AS string)) LIKE %:keyword% " +
            "OR LOWER(CAST(p.dateDefense AS string)) LIKE %:keyword%")
    List<Project> findByKeyword(String keyword);
}
