package pl.demo.zwinne.IntegrationTests;


import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.demo.zwinne.dto.ProjectForm;
import pl.demo.zwinne.model.File;
import pl.demo.zwinne.model.Project;
import pl.demo.zwinne.respository.FileRepository;
import pl.demo.zwinne.service.MinioService;
import pl.demo.zwinne.service.ProjectService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FileControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private MinioService minioService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private FileRepository fileRepository;
    private MinioClient minioClient;
    private Project project;
    @Value("${minio.url}")
    String url;
    @Value("${minio.access-key}")
    String accessKey;
    @Value("${minio.secret-key}")
    String secretKey;

    @BeforeEach
    public void setUp() {
        this.minioClient = MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
        ProjectForm projectForm = new ProjectForm();
        projectForm.setName("Test Project");
        projectForm.setDescription("This is a test project");
        projectForm.setDateCreate(LocalDateTime.now());
        projectForm.setDateDefense(LocalDateTime.now());
        project = projectService.addProject(projectForm);
    }

    @AfterEach
    public void clean() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        projectService.deleteProject(project.getId());
    }

    @Test
    @WithMockUser(username = "test", roles = {"STUDENT"})
    public void addFileAsStudent() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
        mvc.perform(multipart("/api/minio/project/" + project.getId())
                        .file("file", firstFile.getBytes()))
                .andExpect(status().isOk());
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket("mybucket")
                .object(String.valueOf(project.getId()))
                .build());
    }

    @Test
    @WithMockUser(username = "test", roles = {"TEACHER"})
    public void addFileAsTeacher() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
        mvc.perform(multipart("/api/minio/project/" + project.getId())
                        .file("file", firstFile.getBytes()))
                .andExpect(status().isOk());
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket("mybucket")
                .object(String.valueOf(project.getId()))
                .build());
    }

    @Test
    @WithMockUser(username = "test", roles = {"SUPER_ADMIN"})
    public void addFileAsSuperAdmin() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
        mvc.perform(multipart("/api/minio/project/" + project.getId())
                        .file("file", firstFile.getBytes()))
                .andExpect(status().isOk());
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket("mybucket")
                .object(String.valueOf(project.getId()))
                .build());
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void addFileAsAdmin() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
        mvc.perform(multipart("/api/minio/project/" + project.getId())
                        .file("file", firstFile.getBytes()))
                .andExpect(status().isOk());
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket("mybucket")
                .object(String.valueOf(project.getId()))
                .build());
    }

    @Test
    public void addFileAsGuest() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
        mvc.perform(multipart("/api/minio/project/" + project.getId())
                        .file("file", firstFile.getBytes()))
                .andExpect(status().is4xxClientError());
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket("mybucket")
                .object(String.valueOf(project.getId()))
                .build());
    }

    @Test
    public void removeFileAsStudent() throws Exception {
        mvc.perform(delete("/api/minio/project/" + project.getId() + "/file")
                        .param("filename", project.getId().toString() + "filename.txt"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "test", roles = {"TEACHER"})
    public void removeFileAsTeacher() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());

        minioClient.putObject(PutObjectArgs.builder()
                .bucket("mybucket")
                .object(String.valueOf(project.getId()) + firstFile.getOriginalFilename())
                .stream(firstFile.getInputStream(), firstFile.getSize(), -1)
                .build());

        File file = new File(project.getId().toString() + firstFile.getOriginalFilename(), "test_url");
        file.setProject(project);
        fileRepository.save(file);
        project.getFile().add(file);
        projectService.saveProject(project);

        mvc.perform(delete("/api/minio/project/" + project.getId() + "/file")
                        .param("filename", project.getId().toString() + firstFile.getOriginalFilename()))
                .andExpect(status().isOk());

//        JAKIMŚ MAGICZNYM PRAWEM NIE USUWA OBIEKTU I NIE WYRZUCA BŁĘDU TYLKO I WYŁĄCZNIE NA TYM TEŚCIE
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket("mybucket")
                .object(project.getId().toString() + firstFile.getOriginalFilename())
                .build());
    }

    @Test
    @WithMockUser(username = "test", roles = {"STUDENT"})
    public void getAllFilesFromProjectAsStudent() throws Exception {
        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());

        minioClient.putObject(PutObjectArgs.builder()
                .bucket("mybucket")
                .object(String.valueOf(project.getId()) + firstFile.getOriginalFilename())
                .stream(firstFile.getInputStream(), firstFile.getSize(), -1)
                .build());

        File file = new File(project.getId().toString() + firstFile.getOriginalFilename(), "test_url");
        file.setProject(project);
        fileRepository.save(file);
        project.getFile().add(file);
        projectService.saveProject(project);

        mvc.perform(get("/api/minio/project/" + project.getId() + "/file"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].filename", is(file.getFilename())));

        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket("mybucket")
                .object(project.getId().toString() + firstFile.getOriginalFilename())
                .build());
    }

    @Test
    @WithMockUser(username = "test", roles = {""})
    public void getAllFilesFromProjectAsGuest() throws Exception {
        mvc.perform(get("/api/minio/project/" + project.getId() + "/file"))
                .andExpect(status().is4xxClientError());
    }

}
