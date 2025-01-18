package pl.demo.zwinne.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.demo.zwinne.model.File;

import java.util.Optional;

public interface FileRepository  extends JpaRepository<File, Long> {
    File findByFilename(String filename);
}
