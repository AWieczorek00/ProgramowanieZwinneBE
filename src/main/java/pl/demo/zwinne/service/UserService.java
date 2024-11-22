package pl.demo.zwinne.service;


import pl.demo.zwinne.model.User;

import java.util.List;

public interface UserService  {
    User getUserById(Long id);
    List<User> getAll();
}
