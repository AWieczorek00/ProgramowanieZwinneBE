package pl.demo.zwinne.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.demo.zwinne.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u " +
            "WHERE LOWER(u.name) LIKE %:keyword% " +
            "OR LOWER(u.surname) LIKE %:keyword% " +
            "OR LOWER(u.indexNumber) LIKE %:keyword% " +
            "OR LOWER(u.email) LIKE %:keyword% ")
    List<User> findByKeyword(String keyword);
}
