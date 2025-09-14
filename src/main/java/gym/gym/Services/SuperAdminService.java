package gym.gym.Services;

import gym.gym.Models.SuperAdmin;
import gym.gym.Repositories.SuperAdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SuperAdminService {
    @Autowired
    private SuperAdminRepo superAdminRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<SuperAdmin> findByUsername(String username) {
        return superAdminRepo.findByUsername(username);
    }

    public SuperAdmin saveSuperAdmin(SuperAdmin superAdmin) {
        superAdmin.setPassword(passwordEncoder.encode(superAdmin.getPassword()));
        return superAdminRepo.save(superAdmin);
    }

    public boolean authenticate(String username, String password) {
        Optional<SuperAdmin> adminOpt = findByUsername(username);
        return adminOpt.isPresent() && passwordEncoder.matches(password, adminOpt.get().getPassword());
    }
}
