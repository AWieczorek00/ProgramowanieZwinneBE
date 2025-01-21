package pl.demo.zwinne.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.demo.zwinne.Enum.RoleEnum;
import pl.demo.zwinne.dto.LoginUserDto;
import pl.demo.zwinne.dto.RegisterUserDto;
import pl.demo.zwinne.exception.UserAlreadyExistsException;
import pl.demo.zwinne.model.Role;
import pl.demo.zwinne.model.User;
import pl.demo.zwinne.respository.RoleRepository;
import pl.demo.zwinne.respository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder, RoleRepository roleRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User signup(RegisterUserDto input) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.STUDENT);
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());

        if (optionalRole.isEmpty()) {
            return null;
        }

        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        User user = new User(
                input.getName(),
                input.getSurname(),
                input.getEmail(),
                passwordEncoder.encode(input.getPassword()),
                input.getIndexNumber(),
                input.getStationary(),
                optionalRole.get()
        );

        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}
