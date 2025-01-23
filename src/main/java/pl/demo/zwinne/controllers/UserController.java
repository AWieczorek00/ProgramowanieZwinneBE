package pl.demo.zwinne.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.demo.zwinne.Enum.RoleEnum;
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

    @GetMapping("/{email}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> getUser(@PathVariable(name = "email") String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<String> allUsers() {
        return ResponseEntity.ok("All users");
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<User>> getSortedUsers(
            @RequestParam String sortBy,
            @RequestParam(defaultValue = "asc") String order){
        List<User> sortedUsers = userService.getSortedUsers(sortBy, order);
        return ResponseEntity.ok(sortedUsers);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam String searchText) {
        try {
            List<User> filteredUsers = userService.searchUsers(searchText);
            return ResponseEntity.ok(filteredUsers);
        } catch (Exception e) {
            log.error("Error searching users", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("changeRole/{id}/{role}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<User> getUser(@PathVariable(name = "id") Long id, @PathVariable(name = "role") String role) {
        User user = userService.changeRole(id, role);
        return ResponseEntity.ok(user);
    }
}
