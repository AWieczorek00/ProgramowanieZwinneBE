package pl.demo.zwinne.service;


import pl.demo.zwinne.model.User;

import java.util.List;

public interface UserService  {
    void addUser(User user);
    void deleteUser(Long id);
    User getUserById(Long id);
    List<User> getAll();
}
