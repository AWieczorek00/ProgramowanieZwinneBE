package pl.demo.zwinne.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.demo.zwinne.model.User;
import pl.demo.zwinne.service.UserService;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<User>> getAllUser() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('STUDENT', 'SUPER_ADMIN')")
    public ResponseEntity<String> allUsers() {
        return ResponseEntity.ok("All users");
    }
}
