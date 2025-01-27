package pl.demo.zwinne.service;

import org.springframework.stereotype.Service;
import pl.demo.zwinne.model.Role;
import pl.demo.zwinne.respository.RoleRepository;

import java.util.List;

@Service
public class RoleService implements RoleServiceImpl {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }
}