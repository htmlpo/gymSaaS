package gym.gym.Repositories;

import gym.gym.Models.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SuperAdminRepo extends JpaRepository<SuperAdmin, Long> {
    Optional<SuperAdmin> findByUsername(String username);
}
