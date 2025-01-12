package pl.demo.zwinne.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.demo.zwinne.Enum.RoleEnum;
import pl.demo.zwinne.model.Role;
import pl.demo.zwinne.model.User;
import pl.demo.zwinne.respository.RoleRepository;
import pl.demo.zwinne.respository.UserRepository;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;


    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAll() { return userRepository.findAll(); }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User changeRole(Long id, String role) {
        Optional<Role> roleOptional = roleRepository.findByName(RoleEnum.valueOf(role));

        if(roleOptional.isEmpty()) {
            throw new RuntimeException("Role not found");
        }

        return userRepository.findById(id).map(user -> {
            user.setRole(roleOptional.get());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }



    @Override
    public List<User> getSortedUsers(String sortBy, String order) {
        Sort sort = order.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        return userRepository.findAll(sort);
    }

    @Override
    public List<User> searchUsers(String searchText) {
        return userRepository.findByKeyword(searchText.toLowerCase());
    }
}
