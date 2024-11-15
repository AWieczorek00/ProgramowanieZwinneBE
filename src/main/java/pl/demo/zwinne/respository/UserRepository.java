package pl.demo.zwinne.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.demo.zwinne.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
