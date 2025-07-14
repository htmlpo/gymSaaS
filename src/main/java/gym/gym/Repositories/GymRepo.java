package gym.gym.Repositories;


import gym.gym.Models.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GymRepo extends JpaRepository<Gym, Long> {


}
