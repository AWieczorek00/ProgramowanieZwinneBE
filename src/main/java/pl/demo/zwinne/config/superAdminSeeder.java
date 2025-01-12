package pl.demo.zwinne.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.demo.zwinne.Enum.RoleEnum;
import pl.demo.zwinne.model.Role;
import pl.demo.zwinne.model.User;
import pl.demo.zwinne.respository.RoleRepository;
import pl.demo.zwinne.respository.UserRepository;

import java.util.Optional;

@Component
public class superAdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public superAdminSeeder(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private void loadSuperAdmin() {
        Optional<Role> role ;

        role = roleRepository.findByName(RoleEnum.SUPER_ADMIN);
        Optional<User> optionalUser = userRepository.findByEmail("superAdmin@gmail.com");

        if(role.isEmpty()) {
            throw new RuntimeException("Role not found");
        }

        if (optionalUser.isPresent()) {
            return;
        }

        User user = new User();
        user.setEmail("superAdmin@gmail.com");
        user.setPassword(passwordEncoder.encode("sAdmin"));
        user.setName("superAdmin");
        user.setSurname("superAdmin");
        user.setRole(role.get());
        user.setIndexNumber("111111");
        user.setStationary(true);

        userRepository.save(user);
    }

    @Override
    public void run(String... args) throws Exception {
        loadSuperAdmin();
    }
}
