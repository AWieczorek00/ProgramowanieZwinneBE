package pl.demo.zwinne.service;

import org.springframework.web.multipart.MultipartFile;
import pl.demo.zwinne.model.File;

import java.util.List;

public interface MinioService {
    public String add(String filename, MultipartFile file, Long projectID);
    public void delete(String filename, Long projectID);
    public List<File> getAllFileFromProject(Long projectID);
}
