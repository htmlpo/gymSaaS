package gym.gym.Repositories;

import gym.gym.Models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscripitonRepo extends JpaRepository<Subscription,Long> {

    List<Subscription> findByMember_Id(Long memberId);
    List<Subscription> findByGym_Id(Long gymId);
}
