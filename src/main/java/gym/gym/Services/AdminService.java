
package gym.gym.Services;


import gym.gym.Models.Admin;
import gym.gym.Models.Gym;
import gym.gym.Repositories.AdminRepo;
import gym.gym.Repositories.GymRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private AdminRepo adminRepo;
@Autowired
private GymRepo gymRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Authentification d'un admin par username, password et gymId (hash)
    public Optional<Admin> authenticateAdmin(String username, String password, Long gymId) {
        Optional<Admin> adminOpt = adminRepo.findByUsernameAndGym_Id(username, gymId);
        if (adminOpt.isPresent() && passwordEncoder.matches(password, adminOpt.get().getPassword())) {
            return adminOpt;
        }
        return Optional.empty();
    }


    public Admin saveAdmin(Admin admindto) {
        Gym gym = gymRepo.findById(admindto.getGym().getId()).orElse(null);
        Admin admin = new Admin();
        admin.setUsername(admindto.getUsername());
        admin.setPassword(passwordEncoder.encode(admindto.getPassword()));
        admin.setGym(gym);
        return adminRepo.save(admin);
    }

    public List<Admin> getAllAdminsByGymId(Long gymId) {
        return adminRepo.findByGym_Id(gymId);
    }

    public void deleteAdmin (Long id) {

        adminRepo.deleteById(id);
    }

    public boolean updateAdminById(Long id, Long gymId, Admin adminData) {
        Optional<Admin> optionalAdmin = adminRepo.findById(id)
            .filter(admin -> admin.getGym() != null && admin.getGym().getId().equals(gymId));
        if (optionalAdmin.isPresent()) {
            Admin existingAdmin = optionalAdmin.get();
            existingAdmin.setUsername(adminData.getUsername());
            existingAdmin.setPassword(passwordEncoder.encode(adminData.getPassword()));
            adminRepo.save(existingAdmin);
            return true;
        }
        return false;
    }

    public Optional<Admin> getAdminByIdAndGymId(Long id, Long gymId) {
        return adminRepo.findById(id)
            .filter(admin -> admin.getGym() != null && admin.getGym().getId().equals(gymId));
    }


}
