package gym.gym.Services;


import gym.gym.GymApplication;
import gym.gym.Models.Gym;
import gym.gym.Models.Payment;
import gym.gym.Repositories.GymRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GymService {

   @Autowired
    private GymRepo gymRepo;

    public List<Gym> getAllGyms() {
        return gymRepo.findAll();
    }

    public Optional<Gym> getGymByIdAndGymId(Long id, Long gymId) {
        return gymRepo.findById(id)
            .filter(gym -> gym.getId().equals(gymId));
    }

    public Gym saveGym(Gym gym) {
        return gymRepo.save(gym); // ✅ retourne gym
    }

    public void deleteGym (Long id) {
        gymRepo.deleteById(id);
    }








}
