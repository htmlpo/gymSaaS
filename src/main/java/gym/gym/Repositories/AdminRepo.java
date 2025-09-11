package gym.gym.Repositories;

import gym.gym.Models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepo extends JpaRepository<Admin, Long> {
	java.util.List<Admin> findByGym_Id(Long gymId);

	// Pour l'authentification (en clair, à sécuriser ensuite)
	java.util.Optional<Admin> findByUsernameAndGym_Id(String username, Long gymId);
}
