package pl.demo.zwinne.service;

import pl.demo.zwinne.model.User;

import java.util.List;

public interface UserService  {
    void addUser(User user);
    void deleteUser(Long id);
    User getUserById(Long id);
    List<User> getAll();
    User getUserByEmail(String email);
    List<User> getSortedUsers(String sortBy, String order);
    List<User> searchUsers(String searchText);
}
