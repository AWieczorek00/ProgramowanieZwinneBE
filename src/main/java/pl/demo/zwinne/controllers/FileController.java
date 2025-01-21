package pl.demo.zwinne.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.demo.zwinne.model.File;
import pl.demo.zwinne.service.MinioService;

import java.util.List;

@Controller
@RequestMapping("/api/minio")
@PreAuthorize("isAuthenticated()")
@Slf4j
public class FileController {

    @Autowired
    private MinioService minioService;

    //    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','TEACHER', 'STUDENT')")
    @PostMapping("/project/{projectID}")
    public ResponseEntity<String> addFile(@PathVariable Long projectID, @RequestPart(value = "file") MultipartFile file) {
        return ResponseEntity.ok(minioService.add(file.getOriginalFilename(), file, projectID));
    }

    //    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','TEACHER')")
    @DeleteMapping("/project/{projectID}/file")
    public ResponseEntity<Void> deleteFile(@PathVariable Long projectID, @RequestParam String filename) {
        minioService.delete(filename, projectID);
        return ResponseEntity.ok().build();
    }

//    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN','TEACHER', 'STUDENT')")
    @GetMapping("/project/{projectID}/file")
    public ResponseEntity<List<File>> getAllFilesFromProject(@PathVariable Long projectID){
        return ResponseEntity.ok(minioService.getAllFileFromProject(projectID));
    }
}
