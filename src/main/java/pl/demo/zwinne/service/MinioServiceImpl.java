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
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(String.valueOf(projectID)+filename)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());
            log.info("Uploaded file {} to {} bucket", filename, bucketName);
            String signedUrl = getPreSignedUrl(filename, projectID);
            Project project = projectService.getProjectById(projectID);
            File new_file = new File(filename, signedUrl);
            new_file.setProject(project);
            fileRepository.save(new_file);
            project.getFile().add(new_file);
            projectService.saveProject(project);
            return signedUrl;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void delete(String filename, Long projectID) {
        try {
            Project project = projectService.getProjectById(projectID);
            File file = fileRepository.findByFilename(filename);
            project.getFile().remove(file);
            fileRepository.delete(file);
            projectService.saveProject(project);
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(String.valueOf(projectID)+filename)
                    .build());
        } catch (Exception ex) {
            log.info("Usunięte x2 bo tak react chciał");
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
                            .object(String.valueOf(projectID)+filename)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error generating URL for file " + filename, e);
        }
    }
}
