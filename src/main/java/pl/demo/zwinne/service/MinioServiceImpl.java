package pl.demo.zwinne.service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.demo.zwinne.exception.MinioException;
import pl.demo.zwinne.model.File;
import pl.demo.zwinne.model.Project;
import pl.demo.zwinne.respository.FileRepository;

import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class MinioServiceImpl implements MinioService {

    private final MinioClient minioClient;
    private final String bucketName;
    private final ProjectService projectService;
    private final FileRepository fileRepository;

    public MinioServiceImpl(
            @Value("${minio.url}") String url,
            @Value("${minio.access-key}") String accessKey,
            @Value("${minio.secret-key}") String secretKey,
            @Value("${minio.bucket-name}") String bucketName,
            ProjectService projectService,
            FileRepository fileRepository
    ) {
        this.bucketName = bucketName;
        this.minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
        this.projectService = projectService;
        this.fileRepository = fileRepository;
    }

    @Override
    public String add(String filename, MultipartFile file, Long projectID) {
        try {
            String signedUrl = getPreSignedUrl(filename, projectID);
            Project project = projectService.getProjectById(projectID);

            if (project.getFile().stream().anyMatch(file1 -> file1.getFilename().equals(filename)))
                throw new MinioException("Couldn't add file");

            File new_file = new File(filename, signedUrl);
            new_file.setProject(project);
            fileRepository.save(new_file);
            project.getFile().add(new_file);
            projectService.saveProject(project);
            log.info("Uploaded file {} to {} bucket", filename, bucketName);
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(String.valueOf(projectID) + filename)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());
            return signedUrl;
        } catch (Exception ex) {
            throw new MinioException("Couldn't add file");
        }
    }

    @Override
    public void delete(String filename, Long projectID) {
        try {
            Project project = projectService.getProjectById(projectID);
            File file = fileRepository.findByFilename(filename);
            log.info(filename);
            project.getFile().remove(file);
            fileRepository.delete(file);
            projectService.saveProject(project);
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(String.valueOf(projectID) + filename)
                    .build());
        } catch (Exception ex) {
            throw new MinioException("Couldn't remove file");
        }
    }

    @Override
    public List<File> getAllFileFromProject(Long projectID) {
        return projectService.getProjectById(projectID).getFile();
    }

    public String getPreSignedUrl(String filename, Long projectID) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(String.valueOf(projectID) + filename)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error generating URL for file " + filename, e);
        }
    }
}
